package jp.co.acom.riza.event.kafka;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import javax.persistence.EntityManager;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import brave.Tracer;
import brave.propagation.TraceContext;
import jp.co.acom.riza.context.CommonContext;
import jp.co.acom.riza.event.config.EventConfiguration;
import jp.co.acom.riza.event.config.EventConstants;
import jp.co.acom.riza.event.entity.TranExecCheckEntity;
import jp.co.acom.riza.event.msg.EntityEvent;
import jp.co.acom.riza.event.utils.StringUtil;
import jp.co.acom.riza.exception.DuplicateExecuteException;
import jp.co.acom.riza.system.utils.log.Logger;

/**
 * ビジネス動的呼出しプロセス
 *
 * @author teratani
 *
 */
@Service(EntityConsumerInitilizer.PROCESS_ID)
//@Transactional
public class EntityConsumerInitilizer implements Processor {
	/**
	 * ロガー
	 */
	private static Logger logger = Logger.getLogger(EntityConsumerInitilizer.class);

	/**
	 * 当該プロセスID
	 */
	public static final String PROCESS_ID = "entityConsumerInitilizer";
	/**
	 * Producer Template
	 */
	@Autowired
	CommonContext commonContext;

	@Autowired
	private ApplicationContext applicationContext;
	
	@Autowired
	Tracer tracer;

	/**
	 * コンシューマ初期化
	 * 
	 * @throws Exception
	 */
	public void process(Exchange exchange) throws Exception {
		logger.debug("process() started.");

		EntityEvent entityEvent = StringUtil.stringToEntityEventObject((String) exchange.getIn().getBody());
		setCommonContext(exchange.getFromRouteId(), entityEvent);
		insertTranExecChckEntity(commonContext.getReqeustId(), commonContext.getLjcomDateTime());
		exchange.getOut().setBody(exchange.getIn().getBody());
		exchange.getOut().setHeaders(exchange.getIn().getHeaders());
		exchange.getOut().setHeader(EventConstants.EXCHANGE_HEADER_ENTITY_EVENT, entityEvent);
	}

	/**
	 * 共通コンテキストの設定
	 * 
	 * @param routeId CAMELルートID
	 * @param entityEvent イベントエンティティ情報
	 */
	private void setCommonContext(String routeId, EntityEvent entityEvent) {
		logger.debug("setCommonContext() started.");

		LocalDateTime now = LocalDateTime.now();
		commonContext.setLjcomDateTime(now);
		commonContext.setLjcomDate(LocalDate.of(now.getYear(), now.getMonth(), now.getDayOfMonth()));
		commonContext.setLjcomTime(LocalTime.of(now.getHour(), now.getMinute(), now.getSecond()));
		String[] splitStr = routeId.split("_", 4);
		commonContext.setBusinessProcess(splitStr[3]);
		commonContext.setReqeustId(entityEvent.getHeader().getReqeustId() + ":" + commonContext.getBusinessProcess());
		
		TraceContext traceContext = tracer.currentSpan().context();
		commonContext.setTraceId(traceContext.traceIdString());
		commonContext.setSpanId(Long.toHexString(traceContext.spanId()));
		commonContext.setUserId(entityEvent.getHeader().getUserId());
	}

	/**
	 * トランザクション実行テーブルインサート(重複実行チェック)
	 * 
	 * @param key 重複チェックトランザクション識別キー
	 * @param dateTime 日時
	 */
	private void insertTranExecChckEntity(String key, LocalDateTime dateTime) {
		logger.info("insertTranExecChckEntity() started.");

		EntityManager em = (EntityManager) applicationContext.getBean(EventConfiguration.ENTITY_MANAGER_NAME);
		TranExecCheckEntity checkEntity = em.find(TranExecCheckEntity.class, commonContext.getReqeustId());
		if (checkEntity != null) {
			throw new DuplicateExecuteException(checkEntity.toString());
		}
		TranExecCheckEntity execEntity = new TranExecCheckEntity();
		execEntity.setEventKey(commonContext.getReqeustId());
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
		execEntity.setDatetime(commonContext.getLjcomDateTime().format(formatter));
		logger.info("execEntity=" + execEntity);
		em.persist(execEntity);
	}
}

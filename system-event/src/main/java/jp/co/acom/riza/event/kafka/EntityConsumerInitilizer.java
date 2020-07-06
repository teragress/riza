package jp.co.acom.riza.event.kafka;

import java.io.IOException;
import java.util.Date;

import javax.transaction.Transactional;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.kafka.KafkaConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import jp.co.acom.riza.context.CommonContext;
import jp.co.acom.riza.event.msg.EntityEvent;
import jp.co.acom.riza.event.utils.StringUtil;
import jp.co.acom.riza.system.utils.log.Logger;

/**
 * ビジネス動的呼出しプロセス
 *
 * @author developer
 *
 */
@Service
public class EntityConsumerInitilizer implements Processor {
	/**
	 * ロガー
	 */
	private static Logger logger = Logger.getLogger(EntityConsumerInitilizer.class);
	/**
	 * Producer Template
	 */
	@Autowired
	CommonContext commonContext;

	/**
	 * ダイナミック業務呼出し
	 * 
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 */
	public void process(Exchange exchange) throws JsonParseException, JsonMappingException, IOException {
		logger.debug("process() started.");

		EntityEvent entityEvent = StringUtil.stringToEntityEventObject((String) exchange.getIn().getBody());
//		commonContext.setDate(null);
//		commonContext.setFlowid(null);
//		commonContext.setReqeustId(null);
//		commonContext.setSpanId(null);
//		commonContext.setTraceId(null);
//		commonContext.setUserId(null);
	}

}

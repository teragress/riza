package jp.co.acom.riza.event.kafka;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.kafka.KafkaConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.acom.riza.event.cmd.parm.KafkaMessageInfo;
import jp.co.acom.riza.event.config.EventMessageId;
import jp.co.acom.riza.event.utils.StringUtil;
import jp.co.acom.riza.exception.DuplicateExecuteException;
import jp.co.acom.riza.system.utils.log.Logger;
import jp.co.acom.riza.system.utils.log.MessageFormat;

/**
 * ビジネス動的呼出しプロセス
 *
 * @author developer
 *
 */
@Service(DynamicExecuteProcess.PROCESS_ID)
public class DynamicExecuteProcess implements Processor {
	/**
	 * ロガー
	 */
	private static Logger logger = Logger.getLogger(DynamicExecuteProcess.class);

	public static final String PROCESS_ID = "dynamicExecuteProcess";

	/**
	 * Producer Template
	 */
	@Autowired
	ProducerTemplate template;

	@Autowired
	ApplicationRouteHolder holder;

	@Autowired
	TransactionRouteExecute execute;

	/**
	 * ダイナミック業務呼出し
	 */
	public void process(Exchange exchange) {
		logger.debug("process() started.");
		String[] ids = exchange.getFromRouteId().split("_");
		String group = ids[1];
		String topic = exchange.getIn().getHeader(KafkaConstants.TOPIC, String.class);
		for (String root : holder.getApplicationRoutes(group, topic)) {
			logger.info("dynamic execute route=" + root + " group=" + group + " topic=" + topic + " body="
					+ exchange.getIn().getBody());
			try {
				execute.executeRoute("direct:" + root, exchange);
			} catch (Exception ex) {
				if (ex.getCause() != null && ex.getCause() instanceof DuplicateExecuteException) {
					logger.warn(MessageFormat.get(EventMessageId.CONSUMER_ROUTE_DUPLICATION), group,
							getKafkaMessageInfo(topic, exchange));
				} else {

					logger.error(MessageFormat.get(EventMessageId.CONSUMER_ROUTE_EXCEPTION), group, ex.getMessage(),
							getKafkaMessageInfo(topic, exchange));
					logger.error(MessageFormat.get(EventMessageId.EVENT_EXCEPTION), ex);
				}
			}
		}
	}

	/**
	 * @param topic
	 * @param exchange
	 * @return
	 */
	String getKafkaMessageInfo(String topic, Exchange exchange) {
		KafkaMessageInfo msgInfo = new KafkaMessageInfo();
		msgInfo.setTopic(topic);
		msgInfo.setPartition((Integer) exchange.getIn().getHeader(KafkaConstants.PARTITION));
		msgInfo.setOffset((Long) exchange.getIn().getHeader(KafkaConstants.OFFSET));
		return StringUtil.objectToJsonString(msgInfo);
	}
}

package jp.co.acom.riza.event.audit;

import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.component.kafka.KafkaConstants;
import org.apache.camel.component.kafka.KafkaManualCommit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import jp.co.acom.riza.event.config.EventConstants;
import jp.co.acom.riza.event.customer.entity.Customer;
import jp.co.acom.riza.event.msg.EntityEvent;
import jp.co.acom.riza.event.persist.EntityType;
import jp.co.acom.riza.event.utils.AuditEntityUtils;
import jp.co.acom.riza.system.utils.log.Logger;

/**
 * マニュアルコミット
 *
 * @author teratani
 *
 */
@Service(AuditGetProcess.PROC_ID)
public class AuditGetProcess implements Processor {
	/**
	 * ロガー
	 */
	private static Logger logger = Logger.getLogger(AuditGetProcess.class);
	public static final String PROC_ID = "auditGetProcess";

	@Autowired
	AuditEntityUtils auditEntityUtils;

	public void process(Exchange exchange) throws Exception {
		logger.info("*********** ドメイン ************ process() started.");

		EntityEvent entityEvent = (EntityEvent) exchange.getIn().getHeader(EventConstants.EXCHANGE_HEADER_EVENT_OBJECT);
		List<?> customers = auditEntityUtils.getCurrentAndBeforeEntity(entityEvent);
		logger.info("EntityEvent=" + entityEvent);
		if (entityEvent.getEntity().getEntityType() == EntityType.RESOURCE) {
			for (Object entityObj : auditEntityUtils.getCurrentAndBeforeEntity(entityEvent)) {
				logger.info("CurrentAndBefore=" + entityObj.toString());
			}
		}
	}
}

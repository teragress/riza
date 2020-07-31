package jp.co.acom.riza.event.audit;

import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.acom.riza.event.config.EventConstants;
import jp.co.acom.riza.event.customer.entity.Customer;
import jp.co.acom.riza.event.msg.DomainEvent;
import jp.co.acom.riza.event.utils.AuditEntityUtils;
import jp.co.acom.riza.system.utils.log.Logger;

/**
 * マニュアルコミット
 *
 * @author teratani
 *
 */
@Service(AuditDomainGetProcess.PROC_ID)
public class AuditDomainGetProcess implements Processor {
	/**
	 * ロガー
	 */
	private static Logger logger = Logger.getLogger(AuditDomainGetProcess.class);
	public static final String PROC_ID = "auditDomainGetProcess";

	@Autowired
	AuditEntityUtils auditEntityUtils;

	public void process(Exchange exchange) throws Exception {
		logger.info("process() started.");

		DomainEvent domainEvent = (DomainEvent) exchange.getIn().getHeader(EventConstants.EXCHANGE_HEADER_EVENT_OBJECT);
		List<List<Object>> hList = auditEntityUtils.getCurrentAndBeforeEntity(domainEvent, Customer.class.getName());
		for (List<Object> gList : hList) {
			for (Object entityObj : gList) {
				System.out.println(entityObj.toString());
			}
		}
		List<Object> eList = auditEntityUtils.getAuditEntity(domainEvent, Customer.class.getName());
		for (Object entityObj : eList) {
			System.out.println(entityObj.toString());
		}

	}
}

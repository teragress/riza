package jp.co.acom.riza.event.mq;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Session;

import org.springframework.jms.support.destination.DestinationResolver;
import org.springframework.jms.support.destination.DynamicDestinationResolver;

import com.ibm.mq.jms.MQDestination;
import com.ibm.msg.client.wmq.WMQConstants;

/**
 *
 * Dynamic destination resolver which can set some IBM WebSphere MQ specific
 * properties.
 *
 */
public class MQDestinationResolver extends DynamicDestinationResolver implements DestinationResolver {
	private int targetClient = WMQConstants.WMQ_CLIENT_JMS_COMPLIANT;
			
			
			//JMSC.MQJMS_CLIENT_JMS_COMPLIANT;

	/**
	 * @param targetClient Control target client type (JMS compliant or not).
	 *                     <p>
	 *                     Defaults to
	 *                     com.ibm.mq.jms.JMSC.MQJMS_CLIENT_JMS_COMPLIANT
	 */
	public void setTargetClient(int targetClient) {
		this.targetClient = targetClient;
	}

	protected int getTargetClient() {
		return targetClient;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see DestinationResolver#resolveDestinationName(Session, String, boolean)
	 */
	public Destination resolveDestinationName(Session session, String destinationName, boolean isPubSubDomain)
			throws JMSException {
		Destination destination = super.resolveDestinationName(session, destinationName, isPubSubDomain);
		if (destination instanceof MQDestination) {
			MQDestination mqDestination = (MQDestination) destination;
			mqDestination.setTargetClient(getTargetClient());
			mqDestination.setBooleanProperty(WMQConstants.WMQ_MQMD_WRITE_ENABLED,true);
		}
		return destination;
	}
}

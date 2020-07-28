package jp.co.acom.riza.event.mq;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Session;

import org.springframework.jms.support.destination.DestinationResolver;
import org.springframework.jms.support.destination.DynamicDestinationResolver;

import com.ibm.mq.jms.MQDestination;
import com.ibm.msg.client.wmq.WMQConstants;

/**
 * JMSヘッダーを削除するためのRESOLVERクラス
 *
 */
public class MQDestinationResolver extends DynamicDestinationResolver implements DestinationResolver {
	private int targetClient = WMQConstants.WMQ_CLIENT_JMS_COMPLIANT;
	public void setTargetClient(int targetClient) {
		this.targetClient = targetClient;
	}

	/**
	 * @return
	 */
	protected int getTargetClient() {
		return targetClient;
	}

	/**
	 *
	 */
	public Destination resolveDestinationName(Session session, String destinationName, boolean isPubSubDomain)
			throws JMSException {
		Destination destination = super.resolveDestinationName(session, destinationName, isPubSubDomain);
		if (destination instanceof MQDestination) {
			MQDestination mqDestination = (MQDestination) destination;
			mqDestination.setTargetClient(getTargetClient());
			// MSGIDを指定できるようにする
			mqDestination.setBooleanProperty(WMQConstants.WMQ_MQMD_WRITE_ENABLED,true);
		}
		return destination;
	}
}

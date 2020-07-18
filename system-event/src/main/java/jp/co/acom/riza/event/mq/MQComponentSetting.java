package jp.co.acom.riza.event.mq;

import javax.jms.DeliveryMode;
import javax.jms.JMSException;

import org.apache.camel.component.jms.JmsComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.jms.connection.JmsTransactionManager;
import org.springframework.jms.core.JmsOperations;
import org.springframework.jms.core.JmsTemplate;

import com.ibm.mq.MQEnvironment;
import com.ibm.mq.MQSimpleConnectionManager;
import com.ibm.mq.jms.MQQueueConnectionFactory;
import com.ibm.msg.client.wmq.WMQConstants;

/**
 * MQコンポーネントの登録クラス
 *
 * @author developer
 *
 */
@Configuration
public class MQComponentSetting {
	@Autowired
	Environment env;

	/**
	 * MQコンポーネントの登録メソッド
	 *
	 * @return Jmsコンポーネント
	 */
	@Bean(name = "jms")
	public JmsComponent jmsComponent() throws JMSException {
		MQSimpleConnectionManager myConnMan = new MQSimpleConnectionManager();
		myConnMan.setActive(MQSimpleConnectionManager.MODE_AUTO);
		myConnMan.setTimeout(env.getProperty(MQConstants.MQ_CONNECTION_KEEP_TIMEOUT, Integer.class,
				MQConstants.MQ_DEFAULT_CONNECTION_KEEP_TIMEOUT));
		myConnMan.setMaxConnections(
				env.getProperty(MQConstants.MQ_MAX_CONNECTION, Integer.class, MQConstants.MQ_DEFAULT_MAX_CONNECTION));
		myConnMan.setMaxUnusedConnections(env.getProperty(MQConstants.MQ_MAX_UNUSE_CONNECTION, Integer.class,
				MQConstants.MQ_DEFAULT_MAX_UNUSE_CONNECTION));
		MQEnvironment.setDefaultConnectionManager(myConnMan);

		MQQueueConnectionFactory cf = new MQQueueConnectionFactory();
		cf.setHostName(env.getProperty(MQConstants.MQ_CONNECTION_HOST));
		cf.setPort(env.getProperty(MQConstants.MQ_CONNECTION_PORT, Integer.class));
		cf.setTransportType(WMQConstants.WMQ_CM_CLIENT);
		cf.setQueueManager(env.getProperty(MQConstants.MQ_CONNECTION_QMGR));
		cf.setChannel(env.getProperty(MQConstants.MQ_CONNECTION_CHANNEL));
		cf.setStringProperty(WMQConstants.USERID, env.getProperty(MQConstants.MQ_CONNECTION_USER));

		JmsTransactionManager jmsTransactionManager = new JmsTransactionManager();
		jmsTransactionManager.setConnectionFactory(cf);

		JmsComponent jms = JmsComponent.jmsComponentTransacted(cf, jmsTransactionManager);

		jms.setConnectionFactory(cf);
		jms.setStreamMessageTypeEnabled(true);
		jms.setTransacted(true);
//		jms.setConcurrentConsumers(5);
//		jms.setMaxConcurrentConsumers(10);

		MQDestinationResolver resolver = new MQDestinationResolver();
		resolver.setTargetClient(WMQConstants.WMQ_CLIENT_NONJMS_MQ);
		jms.setDestinationResolver(resolver);

		return jms;
	}

	@Bean
	@Primary
	public MQQueueConnectionFactory mqQueueConnectionFactory() throws JMSException {

		MQQueueConnectionFactory cf = new MQQueueConnectionFactory();
		cf.setHostName(env.getProperty(MQConstants.MQ_CONNECTION_HOST));
		cf.setPort(env.getProperty(MQConstants.MQ_CONNECTION_PORT, Integer.class));
		cf.setTransportType(WMQConstants.WMQ_CM_CLIENT);
		cf.setQueueManager(env.getProperty(MQConstants.MQ_CONNECTION_QMGR));
		cf.setChannel(env.getProperty(MQConstants.MQ_CONNECTION_CHANNEL));
		cf.setStringProperty(WMQConstants.USERID, env.getProperty(MQConstants.MQ_CONNECTION_USER));
	
		return cf;
	}
	@Bean
	public JmsOperations jmsOperations(MQQueueConnectionFactory mqQueueConnectionFactory) {
	    JmsTemplate jmsTemplate = new JmsTemplate(mqQueueConnectionFactory);
	    jmsTemplate.setReceiveTimeout(10000);
		MQDestinationResolver resolver = new MQDestinationResolver();
		resolver.setTargetClient(WMQConstants.WMQ_CLIENT_NONJMS_MQ);
		jmsTemplate.setDestinationResolver(resolver);
	    return jmsTemplate;
	}
	/*
	 * @Bean(name = "PROPAGATION_REQUIRED") public SpringTransactionPolicy
	 * propagationRequired() { SpringTransactionPolicy policy = new
	 * SpringTransactionPolicy(); JmsTransactionManager jmsTransactionManager = new
	 * JmsTransactionManager();
	 * policy.setPropagationBehaviorName("PROPAGATION_REQUIRED");
	 * policy.setTransactionManager(jmsTransactionManager); return policy; }
	 */
}

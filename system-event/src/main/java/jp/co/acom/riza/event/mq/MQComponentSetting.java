package jp.co.acom.riza.event.mq;

import javax.jms.JMSException;
import javax.jms.QueueConnectionFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.connection.UserCredentialsConnectionFactoryAdapter;
import org.springframework.jms.core.JmsTemplate;

import com.ibm.mq.jms.MQQueueConnectionFactory;
import com.ibm.msg.client.wmq.WMQConstants;

/**
 * MQコンポーネントの登録クラス
 *
 * @author teratani
 *
 */
@Configuration
public class MQComponentSetting {
	@Autowired
	Environment env;

	/**
	 * キューコネクションファクトリーの設定
	 * @return
	 * @throws JMSException
	 */
	@Bean
	@Primary
	public QueueConnectionFactory mqQueueConnectionFactory() throws JMSException {
		
		MQQueueConnectionFactory cf = new MQQueueConnectionFactory();
		cf.setHostName(env.getProperty(MQConstants.MQ_CONNECTION_HOST));
		cf.setPort(env.getProperty(MQConstants.MQ_CONNECTION_PORT, Integer.class));
		cf.setTransportType(WMQConstants.WMQ_CM_CLIENT);
		cf.setQueueManager(env.getProperty(MQConstants.MQ_CONNECTION_QMGR));
		cf.setChannel(env.getProperty(MQConstants.MQ_CONNECTION_CHANNEL));
	
		return cf;
	}
	
	/**
	 * ユーザー情報設定
	 * @param mqQueueConnectionFactory
	 * @return
	 */
	@Bean
	UserCredentialsConnectionFactoryAdapter userCredentialsConnectionFactoryAdapter(MQQueueConnectionFactory mqQueueConnectionFactory) {
	    UserCredentialsConnectionFactoryAdapter userCredentialsConnectionFactoryAdapter = new UserCredentialsConnectionFactoryAdapter();
	    userCredentialsConnectionFactoryAdapter.setUsername(env.getProperty(MQConstants.MQ_CONNECTION_USER));
	    userCredentialsConnectionFactoryAdapter.setTargetConnectionFactory(mqQueueConnectionFactory);
	    return userCredentialsConnectionFactoryAdapter;
	}
	
	/**
	 * コネクションプーリングを実施するための設定
	 * @param userCredentialsConnectionFactoryAdapter
	 * @return
	 */
	@Bean
	@Primary
	public CachingConnectionFactory cachingConnectionFactory(UserCredentialsConnectionFactoryAdapter userCredentialsConnectionFactoryAdapter) {
	    CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory();
	    cachingConnectionFactory.setTargetConnectionFactory(userCredentialsConnectionFactoryAdapter);
	    cachingConnectionFactory.setSessionCacheSize(env.getProperty(MQConstants.MQ_MAX_CONNECTION, Integer.class, MQConstants.MQ_DEFAULT_MAX_CONNECTION));
	    cachingConnectionFactory.setReconnectOnException(true);
	    cachingConnectionFactory.setCacheProducers(true);
	    return cachingConnectionFactory;
	}

	/**
	 * 送信メソッド用定義
	 * @param cachingConnectionFactory
	 * @return
	 */
	@Bean
	public JmsTemplate jmsOperations(CachingConnectionFactory cachingConnectionFactory) {
	    JmsTemplate jmsTemplate = new JmsTemplate(cachingConnectionFactory);
		MQDestinationResolver resolver = new MQDestinationResolver();
		resolver.setTargetClient(WMQConstants.WMQ_CLIENT_NONJMS_MQ);
		jmsTemplate.setDestinationResolver(resolver);
	    return jmsTemplate;
	}
}

package jp.com.acom.riza.event.mq;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author mtera1003
 *
 */
@Component
public class MessageUtil implements ApplicationContextAware {
	private static ApplicationContext applicationContext;

	/**
	 *
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		MessageUtil.applicationContext = applicationContext;
	}

	/**
	 * @param queName
	 * @param message
	 */
	public static void send(String queName, String message) {
		applicationContext.getBean(MessageUtilImpl.class).pushString(queName, message);
	}
}
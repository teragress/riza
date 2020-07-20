package jp.co.acom.riza.event.kafka;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.UUID;

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
	 * @param queName					MessageUtil.

	 * @param message
	 */
	public static void send(String queName, String message) {
		applicationContext.getBean(MessageUtilImpl.class).pushString(queName, message);
	}
	
	public static byte[] getUniqueID() throws IOException {
		UUID uuid = UUID.randomUUID();
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		byteStream.write(longTobytes(uuid.getMostSignificantBits()));
		byteStream.write(longTobytes(uuid.getLeastSignificantBits()));
		byteStream.write(intTobytes(new Long(System.currentTimeMillis()).hashCode()));
		return byteStream.toByteArray();
	}
	
	public static byte[] longTobytes(Long longValue) {
		return ByteBuffer.allocate(8).putLong(longValue).array();
	}
	public static byte[] intTobytes(Integer intValue) {
		return ByteBuffer.allocate(4).putInt(intValue).array();
	}
	
	public static byte[] createMessageId(byte[]bytes,int index) throws IOException {
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		byteStream.write(bytes);
		byteStream.write(intTobytes(index));
		return byteStream.toByteArray();
	}
}
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
 * メッセージ(MQ)ユーティリティ
 * @author teratani
 */
@Component
public class MessageUtil implements ApplicationContextAware {
	private static ApplicationContext applicationContext;

	/**
	 * アプリケーションコンテキストの設定
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		MessageUtil.applicationContext = applicationContext;
	}

	/**
	 * ビジネス処理から呼び出されるメッセージ送信メソッド
	 * 
	 * @param queName キュー名(トピック名）
	 * @param message 送信メッセージテキスト
	 */
	public static void send(String queName, String message) {
		applicationContext.getBean(MessageHolderUtil.class).pushString(queName, message);
	}
	
	/**
	 * メッセージID用のユニークID生成
	 * @return
	 * @throws IOException
	 */
	public static byte[] getUniqueID() throws IOException {
		UUID uuid = UUID.randomUUID();
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		byteStream.write(longTobytes(uuid.getMostSignificantBits()));
		byteStream.write(longTobytes(uuid.getLeastSignificantBits()));
		byteStream.write(intTobytes(new Long(System.currentTimeMillis()).hashCode()));
		return byteStream.toByteArray();
	}
	
	/**
	 * Long to bytes
	 * @param longValue
	 * @return
	 */
	public static byte[] longTobytes(Long longValue) {
		return ByteBuffer.allocate(8).putLong(longValue).array();
	}
	
	/**
	 * int to bytes
	 * @param intValue
	 * @return
	 */
	public static byte[] intTobytes(Integer intValue) {
		return ByteBuffer.allocate(4).putInt(intValue).array();
	}
	
	/**
	 * メッセージID生成
	 * @param bytes
	 * @param index
	 * @return
	 * @throws IOException
	 */
	public static byte[] createMessageId(byte[]bytes,int index) throws IOException {
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		byteStream.write(bytes);
		byteStream.write(intTobytes(index));
		return byteStream.toByteArray();
	}
}
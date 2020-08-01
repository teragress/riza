package jp.co.acom.riza.event.utils;

import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import jp.co.acom.riza.event.config.EventMessageId;
import jp.co.acom.riza.system.utils.log.Logger;
import jp.co.acom.riza.system.utils.log.MessageFormat;

/**
 * モードユーティリティ
 * 
 * @author teratani
 *
 */
@Service
public class ModeUtil {
	/**
	 * ロガー
	 */
	private static Logger logger = Logger.getLogger(ModeUtil.class);
	/**
	 * KAFKA機能のモック指定キー
	 */
	private static final String KAFKA_MOCK = "KAFKA_MOCK";
	
	/**
	 *　起動モード(ONLINE/BATCH)
	 */
	private static final String EVENT_START_MODE = "EVENT_START_MODE";
	
	/**
	 * 帳票出力(true/false) <br>
	 * デフォルトはtrue
	 */
	private static final String REPORT_OUTPUT = "REPORT_OUTPUT";
	
	/**
	 *　デフォルト起動モード
	 */
	private static final String EVENT_DEFAULT_START_MODE = "ONLINE";
	
	
	/**
	 * イベント機能のモック指定
	 */
	private static final String EVENT_MOCK = "EVENT_MOCK";
	
	/**
	 * DB書き込みのモック指定
	 */
	private static final String EVENT_DB_MOCK = "EVENT_DB_MOCK";
	
	/**
	 * CEPの無効化フラグ(true/false)
	 */
	private static final String CEP_MOCK = "CEP_MOCK";	
	
	@Autowired
	Environment env;
	
	private static boolean kafkaMock = false;
	private static boolean report = true;
	private static boolean cepMock = false;
	private static boolean dbMock = false;
	private static boolean eventMock = false;
	private static boolean online = true;
	
	/**
	 * 
	 */
	@PostConstruct
	void inittialize() {
		kafkaMock = env.getProperty(KAFKA_MOCK,Boolean.class,false);
		report = env.getProperty(REPORT_OUTPUT,Boolean.class,true);
		cepMock = env.getProperty(CEP_MOCK,Boolean.class,false);
		dbMock = env.getProperty(EVENT_DB_MOCK,Boolean.class,false);
		eventMock = env.getProperty(EVENT_MOCK,Boolean.class,false);
		String startMode = env.getProperty(EVENT_START_MODE,EVENT_DEFAULT_START_MODE);
		if (EVENT_DEFAULT_START_MODE.equals(startMode)) {
			online = true;
		} else {
			online = false;
		}
		logger.info(MessageFormat.getMessage(EventMessageId.EVENT_START_INFO, online, report, eventMock, kafkaMock, dbMock, cepMock));
	}
	
	/**
	 * @return
	 */
	public static boolean isKafkaMock() {
		return kafkaMock || eventMock;
	}
	
	/**
	 * @return
	 */
	public static boolean isReportOutput() {
		return report && !eventMock && !kafkaMock;
	}
	
	/**
	 * @return
	 */
	public static boolean isCepMock() {
		return cepMock || eventMock;
	}

	/**
	 * @return
	 */
	public static boolean isDbMock() {
		return dbMock || eventMock;
	}
	
	/**
	 * @return
	 */
	public static boolean isOnline() {
		return online;
	}
	
	/**
	 * @return
	 */
	public static boolean isRouteStart() {
		return !online && !kafkaMock && !eventMock;
	}
}

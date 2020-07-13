package jp.co.acom.riza.event.command;

import jp.co.acom.riza.event.config.EventMessageId;
import jp.co.acom.riza.system.utils.log.Logger;
import jp.co.acom.riza.system.utils.log.MessageFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/** 処理を開始するための REST を受け付ける. */
@RestController
@RequestMapping(path = "event/command", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
public class EventRestCommand {
	/**
	 * ロガー
	 */
	private static Logger logger = Logger.getLogger(EventRestCommand.class);
	
	private static final String REQ_CHECK_POINT_CLEAN = "/CheckPointClean";
	private static final String REQ_EXEC_TABLE_CLEAN = "/ExecTableClean";
	private static final String REQ_EVENT_RECOVERY_DATE_TIME = "/EventRecovery/DateTime";
	private static final String REQ_EVENT_RECOVERY_KEYS = "/EventRecovery/keys";
	private static final String REQ_KAFKA_RECOVERY_OFFSET = "/KafkaRecovery/Offset";
	

  @Autowired
  MessageFormat msg;

  /**
   * 新規登録.
   *
   * @param customer 登録する情報
   * @return
   */
  @RequestMapping(value = REQ_CHECK_POINT_CLEAN)
  public String cleanCheckpoint(@RequestBody CheckpointCleanParm parm) {


    return null;
  }
  /**
   * 新規登録.
   *
   * @param customer 登録する情報
   * @return
   */
  @RequestMapping(value = REQ_EXEC_TABLE_CLEAN)
  public String cleanExecTable(@RequestBody ExecTableCreanParm parm) {


    return null;
  }

  /**
   * 新規登録.
   *
   * @param customer 登録する情報
   * @return
   */
  @RequestMapping(value = REQ_EVENT_RECOVERY_DATE_TIME)
  public String recoveryEventDateTime(@RequestBody EventRecoveryParm parm) {


    return null;
  }
  /**
   * 新規登録.
   *
   * @param customer 登録する情報
   * @return
   */
  @RequestMapping(value = REQ_EVENT_RECOVERY_KEYS)
  public String recoveryEventKeys(@RequestBody EventRecoveryParm parm) {


    return null;
  }
  
  /**
   * 新規登録.
   *
   * @param customer 登録する情報
   * @return
   */
  @RequestMapping(value = REQ_KAFKA_RECOVERY_OFFSET)
  public String recoveryKafkaMessageOffset(@RequestBody EventRecoveryParm parm) {


    return null;
  }
  private void outputStartMessage(String cmd) {
	  logger.info(MessageFormat.get(EventMessageId.EVENT_COMMAND_START));
	  
	  
	  
	  
	  
  }
}

package jp.co.acom.riza.event.command;

import jp.co.acom.riza.event.command.parm.CheckpointCleanParm;
import jp.co.acom.riza.event.command.parm.EventRecoveryParm;
import jp.co.acom.riza.event.command.parm.ExecTableCreanParm;
import jp.co.acom.riza.event.command.parm.KafkaRecoveryParm;
import jp.co.acom.riza.event.command.parm.RouteParm;
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
	private static final String REQ_ROUTE_START = "/Route/Start";
	private static final String REQ_ROUTE_STOP = "/Route/Stop";
	

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
	  outputStartMessage(REQ_CHECK_POINT_CLEAN, parm.toString());


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
	  outputStartMessage(REQ_EXEC_TABLE_CLEAN, parm.toString());


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
	  outputStartMessage(REQ_EVENT_RECOVERY_DATE_TIME, parm.toString());


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
	  outputStartMessage(REQ_EVENT_RECOVERY_KEYS, parm.toString());


    return null;
  }
  
  /**
   * 新規登録.
   *
   * @param customer 登録する情報
   * @return
   */
  @RequestMapping(value = REQ_KAFKA_RECOVERY_OFFSET)
  public String recoveryKafkaMessageOffset(@RequestBody KafkaRecoveryParm parm) {
	  outputStartMessage(REQ_KAFKA_RECOVERY_OFFSET, parm.toString());

    return null;
  }
  /**
   * 新規登録.
   *
   * @param customer 登録する情報
   * @return
   */
  @RequestMapping(value = REQ_KAFKA_RECOVERY_OFFSET)
  public String startRoute(@RequestBody KafkaRecoveryParm parm) {
	  outputStartMessage(REQ_KAFKA_RECOVERY_OFFSET, parm.toString());
	  

    return null;
  }
  
  /**
   * 新規登録.
   *
   * @param customer 登録する情報
   * @return
   */
  @RequestMapping(value = REQ_ROUTE_START)
  public String startRoute(@RequestBody RouteParm parm) {
	  outputStartMessage(REQ_ROUTE_START, parm.toString());
	  

    return null;
  }
  /**
   * 新規登録.
   *
   * @param customer 登録する情報
   * @return
   */
  @RequestMapping(value = REQ_ROUTE_STOP)
  public String stopRoute(@RequestBody RouteParm parm) {
	  outputStartMessage(REQ_ROUTE_STOP, parm.toString());
	  

    return null;
  }
  private void outputStartMessage(String path,String parm) {
	  logger.info(MessageFormat.get(EventMessageId.EVENT_COMMAND_START),path,parm);
  }
}

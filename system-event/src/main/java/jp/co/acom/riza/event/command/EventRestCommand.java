package jp.co.acom.riza.event.command;

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

  @Autowired
  MessageFormat msg;

  /**
   * 新規登録.
   *
   * @param customer 登録する情報
   * @return
   */
  @RequestMapping("/CheckpointClean")
  public String cleanCheckpoint(@RequestBody CheckpointCleanParm parm) {


    return null;
  }
  /**
   * 新規登録.
   *
   * @param customer 登録する情報
   * @return
   */
  @RequestMapping("/ExecTableClean")
  public String cleanExecTable(@RequestBody ExecTableCreanParm parm) {


    return null;
  }

  /**
   * 新規登録.
   *
   * @param customer 登録する情報
   * @return
   */
  @RequestMapping("/EventRecovery/DateTime")
  public String recoveryEventDateTime(@RequestBody EventRecoveryParm parm) {


    return null;
  }
  /**
   * 新規登録.
   *
   * @param customer 登録する情報
   * @return
   */
  @RequestMapping("/EventRecovery/keys")
  public String recoveryEventKeys(@RequestBody EventRecoveryParm parm) {


    return null;
  }
  
  /**
   * 新規登録.
   *
   * @param customer 登録する情報
   * @return
   */
  @RequestMapping("/KafkaRecovery/Offset")
  public String recoveryKafkaMessageOffset(@RequestBody EventRecoveryParm parm) {


    return null;
  }
  
  /**
   * 新規登録.
   *
   * @param customer 登録する情報
   * @return
   */
  @RequestMapping("/KafkaRecovery/Time")
  public String recoveryKafkaMessageTime(@RequestBody EventRecoveryParm parm) {


    return null;
  }

}

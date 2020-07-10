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
@RequestMapping(path = "rest/event", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
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
  @RequestMapping("/eventClean")
  public String cleanEventTalbe(@RequestBody RecoveryCommand command) {


    return null;
  }

  /**
   * 新規登録.
   *
   * @param customer 登録する情報
   * @return
   */
  @RequestMapping("/eventRecovery")
  public String cleanEventTαalbe(@RequestBody CreanCommand customer) {


    return null;
  }

}

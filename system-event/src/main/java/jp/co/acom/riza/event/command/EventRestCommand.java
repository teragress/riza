package jp.co.acom.riza.event.command;

import jp.co.acom.riza.event.command.parm.CheckpointCleanParm;
import jp.co.acom.riza.event.command.parm.EventCommandResponse;
import jp.co.acom.riza.event.command.parm.EventRecoveryParm;
import jp.co.acom.riza.event.command.parm.ExecTableCreanParm;
import jp.co.acom.riza.event.command.parm.KafkaMessageInfo;
import jp.co.acom.riza.event.command.parm.KafkaRecoveryParm;
import jp.co.acom.riza.event.command.parm.RouteParm;
import jp.co.acom.riza.event.config.EventMessageId;
import jp.co.acom.riza.event.kafka.KafkaCommandUtil;
import jp.co.acom.riza.system.utils.log.Logger;
import jp.co.acom.riza.system.utils.log.MessageFormat;

import java.util.List;
import java.util.concurrent.ExecutionException;

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
	private static final String RSP_NORMAL_END = "Normal end.";


	@Autowired
	MessageFormat msg;

	@Autowired
	KafkaCommandUtil kafkaUtil;

	/**
	 * 新規登録.
	 *
	 * @param customer 登録する情報
	 * @return
	 */
	@RequestMapping(path = REQ_CHECK_POINT_CLEAN)
	public EventCommandResponse cleanCheckpoint(@RequestBody CheckpointCleanParm parm) {
		outputStartMessage(REQ_CHECK_POINT_CLEAN, parm.toString());

		EventCommandResponse resp = new EventCommandResponse();
		resp.setRc(EventCommandResponse.RC.OK);
		resp.setMsg(RSP_NORMAL_END);

		return resp;
	}

	/**
	 * 新規登録.
	 *
	 * @param customer 登録する情報
	 * @return
	 */
	@RequestMapping(path = REQ_EXEC_TABLE_CLEAN)
	public EventCommandResponse cleanExecTable(@RequestBody ExecTableCreanParm parm) {
		outputStartMessage(REQ_EXEC_TABLE_CLEAN, parm.toString());

		EventCommandResponse resp = new EventCommandResponse();
		resp.setRc(EventCommandResponse.RC.OK);
		resp.setMsg(RSP_NORMAL_END);

		return resp;
	}

	/**
	 * 新規登録.
	 *
	 * @param customer 登録する情報
	 * @return
	 */
	@RequestMapping(path = REQ_EVENT_RECOVERY_DATE_TIME)
	public EventCommandResponse recoveryEventDateTime(@RequestBody EventRecoveryParm parm) {
		outputStartMessage(REQ_EVENT_RECOVERY_DATE_TIME, parm.toString());

		EventCommandResponse resp = new EventCommandResponse();
		resp.setRc(EventCommandResponse.RC.OK);
		resp.setMsg(RSP_NORMAL_END);

		return resp;
	}

	/**
	 * 新規登録.
	 *
	 * @param customer 登録する情報
	 * @return
	 */
	@RequestMapping(path = REQ_EVENT_RECOVERY_KEYS)
	public EventCommandResponse recoveryEventKeys(@RequestBody EventRecoveryParm parm) {
		outputStartMessage(REQ_EVENT_RECOVERY_KEYS, parm.toString());

		EventCommandResponse resp = new EventCommandResponse();
		resp.setRc(EventCommandResponse.RC.OK);
		resp.setMsg(RSP_NORMAL_END);

		return resp;
	}

	/**
	 * 新規登録.
	 *
	 * @param customer 登録する情報
	 * @return
	 */
	@RequestMapping(path = REQ_KAFKA_RECOVERY_OFFSET)
	public EventCommandResponse recoveryKafkaMessageOffset(@RequestBody KafkaRecoveryParm parm) {
		outputStartMessage(REQ_KAFKA_RECOVERY_OFFSET, parm.toString());
		List<KafkaMessageInfo> rspInfo;
		EventCommandResponse resp = new EventCommandResponse();
		resp.setRc(EventCommandResponse.RC.OK);
		try {
			rspInfo = kafkaUtil.recoveryKafkaMessages(parm.getMsgInfo());
		} catch (InterruptedException | ExecutionException e) {
			// TODO 自動生成された catch ブロック
			resp.setRc(EventCommandResponse.RC.NG);
			resp.setMsg(e.getLocalizedMessage());
			
		}

		resp.setMsg(RSP_NORMAL_END);

		return resp;
	}

	/**
	 * 新規登録.
	 *
	 * @param customer 登録する情報
	 * @return
	 */
	@RequestMapping(path = REQ_ROUTE_START)
	public EventCommandResponse startRoute(@RequestBody RouteParm parm) {
		outputStartMessage(REQ_ROUTE_START, parm.toString());

		EventCommandResponse resp = new EventCommandResponse();
		resp.setRc(EventCommandResponse.RC.OK);
		resp.setMsg(RSP_NORMAL_END);

		return resp;
	}

	/**
	 * 新規登録.
	 *
	 * @param customer 登録する情報
	 * @return
	 */
	@RequestMapping(path = REQ_ROUTE_STOP)
	public EventCommandResponse stopRoute(@RequestBody RouteParm parm) {
		outputStartMessage(REQ_ROUTE_STOP, parm.toString());

		EventCommandResponse resp = new EventCommandResponse();
		resp.setRc(EventCommandResponse.RC.OK);
		resp.setMsg(RSP_NORMAL_END);

		return resp;
	}

	private void outputStartMessage(String path, String parm) {
		logger.info(MessageFormat.get(EventMessageId.EVENT_COMMAND_START), path, parm);
	}
}

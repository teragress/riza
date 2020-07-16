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
import jp.co.acom.riza.event.utils.StringUtil;
import jp.co.acom.riza.system.utils.log.Logger;
import jp.co.acom.riza.system.utils.log.MessageFormat;

import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.camel.CamelContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
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

	private static final String REQ_PREFIX_PATH = "event/command";
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
	ApplicationContext applicationContext;

	@Autowired
	CamelContext camelContext;

	@Autowired
	KafkaCommandUtil kafkaUtil;

	/**
	 * イベントチェックポイントテーブルクリーンナップ
	 * 
	 * @param parm
	 * @return
	 */
	@RequestMapping(path = REQ_CHECK_POINT_CLEAN)
	public EventCommandResponse cleanCheckpoint(@RequestBody CheckpointCleanParm parm) {
		outputStartMessage(REQ_CHECK_POINT_CLEAN, parm.toString());

		EventCommandResponse resp = new EventCommandResponse();
		resp.setRc(EventCommandResponse.RC.OK);
		resp.setMsg(RSP_NORMAL_END);

		outputEndMessage(REQ_KAFKA_RECOVERY_OFFSET, resp.toString());
		return resp;
	}

	/**
	 * イベント実行テーブルクリーンナップ
	 * 
	 * @param parm
	 * @return
	 */
	@RequestMapping(path = REQ_EXEC_TABLE_CLEAN)
	public EventCommandResponse cleanExecTable(@RequestBody ExecTableCreanParm parm) {
		outputStartMessage(REQ_EXEC_TABLE_CLEAN, parm.toString());

		EventCommandResponse resp = new EventCommandResponse();
		resp.setRc(EventCommandResponse.RC.OK);
		resp.setMsg(RSP_NORMAL_END);

		outputEndMessage(REQ_KAFKA_RECOVERY_OFFSET, resp.toString());
		return resp;
	}

	/**
	 * イベントリカバリー（日時指定）
	 * 
	 * @param parm
	 * @return
	 */
	@RequestMapping(path = REQ_EVENT_RECOVERY_DATE_TIME)
	public EventCommandResponse recoveryEventDateTime(@RequestBody EventRecoveryParm parm) {
		outputStartMessage(REQ_EVENT_RECOVERY_DATE_TIME, parm.toString());

		EventCommandResponse resp = new EventCommandResponse();
		resp.setRc(EventCommandResponse.RC.OK);
		resp.setMsg(RSP_NORMAL_END);

		outputEndMessage(REQ_KAFKA_RECOVERY_OFFSET, resp.toString());
		return resp;
	}

	/**
	 * イベントリカバリー（個別（キー）指定）
	 * 
	 * @param parm
	 * @return
	 */
	@RequestMapping(path = REQ_EVENT_RECOVERY_KEYS)
	public EventCommandResponse recoveryEventKeys(@RequestBody EventRecoveryParm parm) {
		outputStartMessage(REQ_EVENT_RECOVERY_KEYS, parm.toString());

		EventCommandResponse resp = new EventCommandResponse();
		resp.setRc(EventCommandResponse.RC.OK);
		resp.setMsg(RSP_NORMAL_END);

		outputEndMessage(REQ_KAFKA_RECOVERY_OFFSET, resp.toString());
		return resp;
	}

	/**
	 * KAFKAメッセージリカバリー(再送信）
	 * 
	 * @param parm
	 * @return
	 */
	@RequestMapping(path = REQ_KAFKA_RECOVERY_OFFSET)
	public EventCommandResponse recoveryKafkaMessageOffset(@RequestBody KafkaRecoveryParm parm) {
		outputStartMessage(REQ_KAFKA_RECOVERY_OFFSET, parm.toString());
		List<KafkaMessageInfo> rspInfo;
		EventCommandResponse resp = new EventCommandResponse();
		resp.setRc(EventCommandResponse.RC.OK);
		try {
			resp = new EventCommandResponse();
			resp.setRc(EventCommandResponse.RC.OK);
			rspInfo = kafkaUtil.recoveryKafkaMessages(parm.getMsgInfo());
			logger.info(MessageFormat.get(EventMessageId.KAFKA_MESSAGE_RECOVERY),
					StringUtil.objectToJsonString(rspInfo));
		} catch (InterruptedException | ExecutionException e) {
			// TODO 自動生成された catch ブロック
			resp.setRc(EventCommandResponse.RC.NG);
			resp.setMsg(e.getLocalizedMessage());
		}

		resp.setMsg(RSP_NORMAL_END);
		outputEndMessage(REQ_KAFKA_RECOVERY_OFFSET, resp.toString());

		return resp;
	}

	/**
	 * // TODO 自動生成された catch ブロック resp.setRc(EventCommandResponse.RC.NG);
	 * resp.setMsg(e.getLocalizedMessage()); ルート開始
	 * 
	 * @param parm EventCommandResponse resp = new EventCommandResponse();
	 *             resp.setRc(EventCommandResponse.RC.OK);
	 * @return
	 */
	@RequestMapping(path = REQ_ROUTE_START)
	public EventCommandResponse startRoute(@RequestBody RouteParm parm) {
		outputStartMessage(REQ_ROUTE_START, parm.toString());
		EventCommandResponse resp = new EventCommandResponse();
		resp.setRc(EventCommandResponse.RC.OK);
		try {
			camelContext.startRoute(parm.getRouteId());
		} catch (Exception ex) {
			// TODO 自動生成された catch ブロック
			resp.setRc(EventCommandResponse.RC.NG);
			resp.setMsg(ex.getLocalizedMessage());
		}

		resp.setRc(EventCommandResponse.RC.OK);
		resp.setMsg(RSP_NORMAL_END);

		outputEndMessage(REQ_KAFKA_RECOVERY_OFFSET, resp.toString());
		return resp;
	}

	/**
	 * ルート停止
	 * 
	 * @param parm
	 * @return
	 */
	@RequestMapping(path = REQ_ROUTE_STOP)
	public EventCommandResponse stopRoute(@RequestBody RouteParm parm) {
		outputStartMessage(REQ_ROUTE_STOP, parm.toString());

		EventCommandResponse resp = new EventCommandResponse();
		resp.setRc(EventCommandResponse.RC.OK);
		resp.setMsg(RSP_NORMAL_END);

		outputEndMessage(REQ_KAFKA_RECOVERY_OFFSET, resp.toString());
		return resp;
	}

	/**
	 * コマンド開始メッセージ出力
	 * 
	 * @param path
	 * @param parm
	 */
	private void outputStartMessage(String path, String parm) {
		logger.info(MessageFormat.get(EventMessageId.EVENT_COMMAND_START), REQ_PREFIX_PATH + path, parm);
	}

	/**
	 * コマンド終了メッセージ出力
	 * 
	 * @param path
	 * @param parm
	 */
	private void outputEndMessage(String path, String parm) {
		logger.info(MessageFormat.get(EventMessageId.EVENT_COMMAND_END), REQ_PREFIX_PATH + path, parm);
	}
}

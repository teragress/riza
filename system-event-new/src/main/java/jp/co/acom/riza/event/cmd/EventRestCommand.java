package jp.co.acom.riza.event.cmd;

import jp.co.acom.riza.event.cmd.parm.CheckpointCleanParm;
import jp.co.acom.riza.event.cmd.parm.EventCommandResponse;
import jp.co.acom.riza.event.cmd.parm.EventRecoveryParm;
import jp.co.acom.riza.event.cmd.parm.ExecTableCreanParm;
import jp.co.acom.riza.event.cmd.parm.KafkaMessageInfo;
import jp.co.acom.riza.event.cmd.parm.KafkaRecoveryParm;
import jp.co.acom.riza.event.cmd.parm.RouteParm;
import jp.co.acom.riza.event.config.EventMessageId;
import jp.co.acom.riza.event.kafka.KafkaEventUtil;
import jp.co.acom.riza.event.utils.StringUtil;
import jp.co.acom.riza.system.utils.log.Logger;
import jp.co.acom.riza.system.utils.log.MessageFormat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.apache.camel.CamelContext;
import org.apache.camel.ServiceStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * イベントコマンド受け付け処理(REST)
 * 
 * @author teratani
 *
 */
@RestController
@RequestMapping(path = "event/command", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
public class EventRestCommand {
	/**
	 * ロガー
	 */
	private static Logger logger = Logger.getLogger(EventRestCommand.class);

	private static final String REQ_PREFIX_PATH = "event/command";
	private static final String REQ_CHECK_POINT_CLEAN = "/CheckPointTableClean";
	private static final String REQ_EXEC_TABLE_CLEAN = "/ExecTableClean";
	private static final String REQ_EVENT_RECOVERY_DATE_TIME = "/EventRecovery/DateTime";
	private static final String REQ_EVENT_RECOVERY_KEYS = "/EventRecovery/keys";
	private static final String REQ_KAFKA_RECOVERY_OFFSET = "/KafkaRecovery/Offset";
	private static final String REQ_ROUTE_START = "/Route/Start";
	private static final String REQ_ROUTE_STOP = "/Route/Stop";
	private static final String RSP_NORMAL_END = "Normal end.";

	private static final int DEFAULT_KEEP_DAYS = 1;
	private static final int DEFAULT_DELETION_SPLIT_COUNT = 1000;

	@Autowired
	MessageFormat msg;

	@Autowired
	ApplicationContext applicationContext;

	@Autowired
	CamelContext camelContext;

	@Autowired
	KafkaEventUtil kafkaEventUtil;

	@Autowired
	EventRecovery eventRecovery;

	@Autowired
	CheckPointTableClean checkPointTableClean;

	@Autowired
	ExecTableClean execTableClean;

	/**
	 * イベントチェックポイントテーブルクリーンナップ
	 * 
	 * @param parm コマンドパラメータ
	 * @return
	 */
	@RequestMapping(path = REQ_CHECK_POINT_CLEAN)
	public EventCommandResponse cleanCheckpoint(@RequestBody CheckpointCleanParm parm) {
		outputStartMessage(REQ_CHECK_POINT_CLEAN, parm.toString());

		try {

			// 保存日数
			int keepDay = DEFAULT_KEEP_DAYS;
			if (parm.getKeepDays() != null) {
				keepDay = parm.getKeepDays();
			}

			// 削除基準日付
			LocalDateTime baseDatetime = LocalDateTime.now();
			if (parm.getBaseDatetime() != null) {
				DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
				baseDatetime = LocalDateTime.parse(parm.getBaseDatetime(), dtf);
			}

			baseDatetime = baseDatetime.minusDays(keepDay);
			
			// トータル削除件数
			int allDeleteCount = 0;
			
			// 分割削除件数
			int splitCount = DEFAULT_DELETION_SPLIT_COUNT;
			if (parm.getDeletionSplitCount() != null) {
				splitCount = parm.getDeletionSplitCount();
			}
			int deleteCount = -1;

			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
			String baseDatetimeStr = baseDatetime.format(formatter);

			// すべて削除できるまでクリーンナップ処理呼出
			while (deleteCount != 0) {
				deleteCount = checkPointTableClean.cleanCheckPoint(baseDatetimeStr, splitCount);
				allDeleteCount = allDeleteCount + deleteCount;
			}
			
			logger.info(MessageFormat.getMessage(EventMessageId.CHECKPOINT_CLEANUP, allDeleteCount));
			
		} catch (Exception ex) {
			return exceptionProc(ex, REQ_CHECK_POINT_CLEAN, parm);
		}

		outputEndMessage(REQ_CHECK_POINT_CLEAN, parm.toString());
		return createNormalResponse(null);
	}

	/**
	 * イベント実行テーブルクリーンナップ
	 * 
	 * @param parm コマンドパラメータ
	 * @return
	 */
	@RequestMapping(path = REQ_EXEC_TABLE_CLEAN)
	public EventCommandResponse cleanExecTable(@RequestBody ExecTableCreanParm parm) {
		outputStartMessage(REQ_EXEC_TABLE_CLEAN, parm.toString());

		try {

			int keepDay = DEFAULT_KEEP_DAYS;
			if (parm.getKeepDays() != null) {
				keepDay = parm.getKeepDays();
			}

			LocalDateTime baseDatetime = LocalDateTime.now();
			if (parm.getBaseDatetime() != null) {
				DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
				baseDatetime = LocalDateTime.parse(parm.getBaseDatetime(), dtf);
			}

			baseDatetime = baseDatetime.minusDays(keepDay);
			int allDeleteCount = 0;
			int splitCount = DEFAULT_DELETION_SPLIT_COUNT;
			if (parm.getDeletionSplitCount() != null) {
				splitCount = parm.getDeletionSplitCount();
			}
			int deleteCount = -1;

			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
			String baseDatetimeStr = baseDatetime.format(formatter);

			while (deleteCount != 0) {
				deleteCount = execTableClean.cleanTranExec(baseDatetimeStr, splitCount);
				allDeleteCount = allDeleteCount + deleteCount;
			}

			logger.info(MessageFormat.getMessage(EventMessageId.TRANEXEC_CLEANUP, allDeleteCount));

		} catch (Exception ex) {
			return exceptionProc(ex, REQ_EXEC_TABLE_CLEAN, parm);
		}

		outputEndMessage(REQ_EXEC_TABLE_CLEAN, parm.toString());
		return createNormalResponse(null);
	}

	/**
	 * イベントリカバリー（日時指定）
	 * 
	 * @param parm コマンドパラメータ
	 * @return
	 */
	@RequestMapping(path = REQ_EVENT_RECOVERY_DATE_TIME)
	public EventCommandResponse recoveryEventDateTime(@RequestBody EventRecoveryParm parm) {
		outputStartMessage(REQ_EVENT_RECOVERY_DATE_TIME, parm.toString());
		try {
			eventRecovery.rangeRecovery(parm);

		} catch (Exception ex) {
			return exceptionProc(ex, REQ_EVENT_RECOVERY_DATE_TIME, parm);
		}

		outputEndMessage(REQ_EVENT_RECOVERY_DATE_TIME, parm.toString());
		return createNormalResponse(null);
	}

	/**
	 * イベントリカバリー（個別（キー）指定）
	 * 
	 * @param parm コマンドパラメータ
	 * @return
	 */
	@RequestMapping(path = REQ_EVENT_RECOVERY_KEYS)
	public EventCommandResponse recoveryEventKeys(@RequestBody EventRecoveryParm parm) {
		outputStartMessage(REQ_EVENT_RECOVERY_KEYS, parm.toString());
		try {
			eventRecovery.keyRecovery(parm);

		} catch (Exception ex) {
			return exceptionProc(ex, REQ_EVENT_RECOVERY_KEYS, parm);
		}

		outputEndMessage(REQ_EVENT_RECOVERY_KEYS, parm.toString());
		return createNormalResponse(null);
	}

	/**
	 * KAFKAメッセージリカバリー(再送信）
	 * 
	 * @param parm コマンドパラメータ
	 * @return
	 */
	@RequestMapping(path = REQ_KAFKA_RECOVERY_OFFSET)
	public EventCommandResponse recoveryKafkaMessageOffset(@RequestBody KafkaRecoveryParm parm) {
		outputStartMessage(REQ_KAFKA_RECOVERY_OFFSET, parm.toString());

		List<KafkaMessageInfo> rspInfo;

		try {
			rspInfo = kafkaEventUtil.recoveryKafkaMessages(parm.getMsgInfo());
			logger.info(MessageFormat.getMessage(EventMessageId.KAFKA_MESSAGE_RECOVERY),
					StringUtil.objectToJsonString(rspInfo));
		} catch (Exception ex) {
			return exceptionProc(ex, REQ_KAFKA_RECOVERY_OFFSET, parm);
		}

		outputEndMessage(REQ_KAFKA_RECOVERY_OFFSET, parm.toString());

		return createNormalResponse(null);
	}

	/**
	 * ルート開始
	 * 
	 * @param parm コマンドパラメータ
	 * @return
	 */
	@RequestMapping(path = REQ_ROUTE_START)
	public EventCommandResponse startRoute(@RequestBody RouteParm parm) {
		outputStartMessage(REQ_ROUTE_START, parm.toString());

		try {
			ServiceStatus sts = camelContext.getRouteStatus(parm.getRouteId());
			if (sts == null) {

			}
			camelContext.startRoute(parm.getRouteId());
		} catch (Exception ex) {
			return exceptionProc(ex, REQ_ROUTE_START, parm);
		}

		outputEndMessage(REQ_ROUTE_START, parm.toString());
		return createNormalResponse(null);
	}

	/**
	 * ルート停止
	 * 
	 * @param parm コマンドパラメータ
	 * @return
	 */
	@RequestMapping(path = REQ_ROUTE_STOP)
	public EventCommandResponse stopRoute(@RequestBody RouteParm parm) {
		outputStartMessage(REQ_ROUTE_STOP, parm.toString());

		try {
			camelContext.stopRoute(parm.getRouteId());
		} catch (Exception ex) {
			return exceptionProc(ex, REQ_ROUTE_STOP, parm);
		}

		outputEndMessage(REQ_ROUTE_STOP, parm.toString());
		return createNormalResponse(null);
	}

	/**
	 * コマンド開始メッセージ出力
	 * 
	 * @param cmdPath コマンドパス
	 * @param parm コマンドパラメータ
	 */
	private void outputStartMessage(String path, String parm) {
		logger.info(MessageFormat.getMessage(EventMessageId.EVENT_COMMAND_START, REQ_PREFIX_PATH + path, parm));
	}

	/**
	 * コマンド終了メッセージ出力
	 * 
	 * @param cmdPath コマンドパス
	 * @param parm コマンドパラメータ
	 */
	private void outputEndMessage(String path, String parm) {
		logger.info(MessageFormat.getMessage(EventMessageId.EVENT_COMMAND_END, REQ_PREFIX_PATH + path, parm));
	}

	/**
	 * 例外共通処理
	 * 
	 * @param ex 例外
	 * @param cmdPath コマンドパス
	 * @param parm コマンドパラメータ
	 * @return
	 */
	private EventCommandResponse exceptionProc(Exception ex, String cmdPath, Object parm) {
		logger.error(MessageFormat.getMessage(EventMessageId.COMMAND_EXCEPTION, REQ_PREFIX_PATH + cmdPath, parm.toString()),ex);

		EventCommandResponse resp = new EventCommandResponse();
		resp.setRc(EventCommandResponse.RC.NG);
		resp.setMsg(ex.getMessage());
		return resp;
	}

	/**
	 * イベントコマンド応答編集
	 * @param addMessage 付加メッセージ
	 * @return  EventCommandResponse
	 */
	private EventCommandResponse createNormalResponse(String addMessage) {
		EventCommandResponse resp = new EventCommandResponse();
		resp.setRc(EventCommandResponse.RC.OK);
		if (addMessage == null || addMessage.length() == 0) {
			resp.setMsg(RSP_NORMAL_END);
		} else {
			resp.setMsg(addMessage);
		}
		return resp;
	}
}

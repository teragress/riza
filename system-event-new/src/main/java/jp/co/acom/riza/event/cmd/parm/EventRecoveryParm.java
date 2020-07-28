package jp.co.acom.riza.event.cmd.parm;

import lombok.Data;

/**
 * イベントリカバリーコマンドパラメーター
 * @author teratani
 *
 */
@Data
public class EventRecoveryParm {
	/**
	 * 対象基準日時
	 */
	private String dateTime;
	
	/**
	 * 範囲指定のTO日時
	 */
	private String toDateTime;
	
	/**
	 * トランザクションID 
	 */
	private String tranid;
	
}

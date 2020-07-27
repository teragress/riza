package jp.co.acom.riza.event.cmd.parm;

import lombok.Data;

/**
 * イベントコマンド応答
 * @author teratani
 *
 */
@Data
public class EventCommandResponse {
	/**
	 * リターンコード列挙値
	 * @author teratani
	 *
	 */
	public enum RC {OK,NG};
	
	/**
	 * リターンコード
	 */
	private RC rc;
	
	/**
	 * 応答メッセージ
	 */
	private String msg;
}

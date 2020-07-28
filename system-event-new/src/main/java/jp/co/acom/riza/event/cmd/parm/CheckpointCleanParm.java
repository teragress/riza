package jp.co.acom.riza.event.cmd.parm;

import lombok.Data;

/**
 * チェックポイントテーブルクリーンナップパラメーター
 * @author teratani
 *
 */
@Data
public class CheckpointCleanParm {
	/**
	 * 基準日時 
	 */
	private String baseDatetime;
	
	/**
	 * 保存日数
	 */
	private Integer keepDays;
	
	/**
	 * 分割削除件数
	 */
	private Integer deletionSplitCount;
}

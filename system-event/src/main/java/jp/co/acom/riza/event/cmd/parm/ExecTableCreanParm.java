package jp.co.acom.riza.event.cmd.parm;

import lombok.Data;

/**
 * トランザクション実行テーブルクリーンナップコマンドパラメーター
 * @author teratani
 *
 */
@Data
public class ExecTableCreanParm {
	/**
	 * 基準日時
	 */
	private String baseDatetime;
	
	/**
	 * 保存日数
	 */
	private Integer keepDays;
	
	/**
	 * 削除分割件数
	 */
	private Integer deletionSplitCount;

}

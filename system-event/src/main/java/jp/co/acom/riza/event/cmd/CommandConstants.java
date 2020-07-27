package jp.co.acom.riza.event.cmd;

/**
 * コマンド用のコンスタント定義
 *
 * @author developer
 *
 */
public interface CommandConstants {
	/**
	 *　チェックポイントクリーンナップの保存日数取得キー
	 */
	public static final String COMMAND_CHECKPOINT_KEEP_DAYS = "COMMAND_CHECKPOINT_KEEP_DAYS";
	
	/**
	 *　デフオルトチェックポイントクリーンナップの保存日数取得キー
	 */
	public static final Integer DEFAULT_COMMAND_CHECKPOINT_KEEP_DAYS = 1;

}

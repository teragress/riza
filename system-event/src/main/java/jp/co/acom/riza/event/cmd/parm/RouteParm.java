package jp.co.acom.riza.event.cmd.parm;

import lombok.Data;

/**
 * CAMELルート開始停止コマンド用パラメーター
 * @author teratani
 *
 */
@Data
public class RouteParm {
	/**
	 * ルートID 
	 */
	private String routeId;
}

package jp.co.acom.riza.event.cmd.parm;

import lombok.Data;

/**
 * KAFKAメッセージ情報
 * @author teratani
 *
 */
@Data
public class KafkaMessageInfo {
	/**
	 * トピック名
	 */
	private String topic;
	
	/**
	 * オフセット
	 */
	private Long offset;
	
	/**
	 * パーティション
	 */
	private Integer partition;
}

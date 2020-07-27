package jp.co.acom.riza.event.msg;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * kAFKAメッセージ情報
 * 
 * @author teratani
 *
 */
@Getter
@Setter
@ToString
public class KafkaMessage {
	/**
	 * パーティション
	 */
	@JsonDeserialize(contentAs = Integer.class)
	private Integer partition;
	/**
	 * オフセット
	 */
	@JsonDeserialize(contentAs = Long.class)
	private Long offset;
}

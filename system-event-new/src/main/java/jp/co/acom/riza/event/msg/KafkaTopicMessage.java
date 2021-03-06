package jp.co.acom.riza.event.msg;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 更新エンティティ情報
 * 
 * @author teratani
 *
 */
@Getter
@Setter
@ToString
public class KafkaTopicMessage {
	/**
	 * パーシステントタイプ(挿入/更新/削除)
	 * 
	 */
	@JsonDeserialize(contentAs = String.class)
	private String topic;

	/**
	 * エンティティマネージャー単位の更新情報リスト
	 */
	@JsonDeserialize(as = ArrayList.class,contentAs = KafkaMessage.class)
	private List<KafkaMessage> kmsg = new ArrayList<KafkaMessage>();

}

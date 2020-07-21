package jp.co.acom.riza.event.msg;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 更新エンティティ情報
 * 
 * @author vagrant
 *
 */
@Getter
@Setter
@ToString
public class KafkaTopics {
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
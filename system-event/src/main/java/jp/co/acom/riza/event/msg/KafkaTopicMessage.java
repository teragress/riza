package jp.co.acom.riza.event.msg;

import java.io.Serializable;
import java.util.List;

import org.springframework.core.serializer.Deserializer;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import jp.co.acom.riza.event.core.EntityType;
import jp.co.acom.riza.event.core.PersistentType;
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
	@JsonTypeInfo(use = Id.CLASS)
	private List<KafkaMessage> kmsg;

}

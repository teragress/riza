package jp.co.acom.riza.event.msg;

import java.io.Serializable;

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
 * kAFKAメッセージ情報
 * 
 * @author vagrant
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

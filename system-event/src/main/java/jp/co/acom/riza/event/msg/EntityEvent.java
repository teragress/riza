package jp.co.acom.riza.event.msg;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * エンティティイベントメッセージ
 * @author teratani
 *
 */
/**
 * @author teratani
 *
 */
/**
 * @author teratani
 *
 */
@Getter
@Setter
@ToString
public class EntityEvent {
	public enum EntityType {
		RESOURCE, EVENT
	}

	/**
	 * ヘッダー
	 */
	@JsonDeserialize(contentAs = Header.class)
	private Header header;
	
	/**
	 * エンティティマネージャーr名
	 */
	@JsonDeserialize(contentAs = String.class)
	private String manager;
	
	/**
	 * エンティティ情報
	 */
	@JsonDeserialize(contentAs = Entity.class)
	private Entity entity;
	
	/**
	 * リビジョン番号
	 */
	@JsonDeserialize(contentAs = Long.class)
	private Long revision;
}

package jp.co.acom.riza.event.core;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 変更時のイベント情報
 * @author vagrant
 *
 */
@Getter
@Setter
@ToString
public class PersistentEvent {
	public PersistentEvent() {
	}

	/**
	 * 変更の種類と対象エンティティを指定してオブジェクトを作成する.
	 * @param persistentType 変更の種類(挿入/更新/削除)
	 * @param entityType エンティティの種類(リソース/イベント)
	 * @param entity 対象エンティティ
	 */
	public PersistentEvent(PersistentType persistentType, EntityType entityType, Object entity, Serializable entityId) {
		this.persistentType = persistentType;
		this.entityType = entityType;
		this.entity = entity;
		this.entityId = entityId;
	}

	/**
	* 変更の種類
	*/
	private PersistentType persistentType;
	/**
	* エンティティの種類
	*/
	private EntityType entityType;
	/**
	*　エンティティのId(キー)
	*/
	private Serializable entityId;
	/**
	 * エンティティオブジェクト
	 */
	private Object entity;
}

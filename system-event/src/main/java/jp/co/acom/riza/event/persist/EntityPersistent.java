package jp.co.acom.riza.event.persist;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 変更時のイベント情報
 * @author teratani
 *
 */
@Getter
@Setter
@ToString
public class EntityPersistent {
	public EntityPersistent() {
	}

	/**
	 * 変更の種類と対象エンティティを指定してオブジェクトを作成する.
	 * @param persistentType 変更の種類(挿入/更新/削除)
	 * @param entityType エンティティの種類(リソース/イベント)
	 * @param entity 対象エンティティ
	 */
	public EntityPersistent(PersistentType persistentType, EntityType entityType, Object entity, Serializable entityId) {
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

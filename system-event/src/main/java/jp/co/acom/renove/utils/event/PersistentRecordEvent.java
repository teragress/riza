package jp.co.acom.renove.utils.event;

import java.io.Serializable;
import lombok.Data;

/** データ変更時のイベント情報. */
@Data
public class PersistentRecordEvent {
	/** 変更の種類. */
	public enum UpdateType {
		UPDATE, INSERT, DELETE
	}

	public enum EventType {
		EVENT, RESOURCE
	}

	public PersistentRecordEvent() {
	}

	/**
	 * 変更の種類と対象エンティティを指定してオブジェクトを作成する.
	 *
	 * @param type   変更の種類
	 * @param entity 対象エンティティ
	 */
	public PersistentRecordEvent(EventType eventType, UpdateType updateType, Object entity) {
		this.eventType = eventType;
		this.updateType = updateType;
	}

	private EventType eventType;
	private UpdateType updateType;
	private Serializable entityId;
	private String EntityClassName;
	private Integer revtion;
}

package jp.co.acom.renove.event.msg;

import jp.co.acom.renove.event.core.PersistentType;
import lombok.Data;

@Data
public class EntityPersistent {
	public enum EntityType {
		RSOURCE, EVENT
	}

	private PersistentType persitenceEventType;
	private EntityType entityType;
	private String entityClassName;
	private String keyClassName;
	private String keyObject;
	private Long revision;
}

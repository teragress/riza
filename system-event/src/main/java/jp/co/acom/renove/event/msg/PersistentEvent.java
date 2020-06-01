package jp.co.acom.renove.event.msg;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class PersistentEvent {
	public enum EntityType {
		RESOURCE, EVENT
	}
	private EventHeader eventHeader;
	private String entityManagerName;
	private EntityType entityType;
	private EntityPersistent entityPersistence;
}

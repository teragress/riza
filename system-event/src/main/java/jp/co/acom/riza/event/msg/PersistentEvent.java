package jp.co.acom.riza.event.msg;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PersistentEvent {
	public enum EntityType {
		RESOURCE, EVENT
	}
	private EventHeader eventHeader;
	private String entityManagerName;
	private PersistentEntity entityPersistence;
}

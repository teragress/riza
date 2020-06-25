package jp.co.acom.riza.event.msg;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class EntityEvent {
	public enum EntityType {
		RESOURCE, EVENT
	}
	private Header header;
	private String manager;
	private Entity entity;
}

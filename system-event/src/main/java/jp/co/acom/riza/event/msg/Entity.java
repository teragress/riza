package jp.co.acom.riza.event.msg;

import java.io.Serializable;

import jp.co.acom.riza.event.core.EntityType;
import jp.co.acom.riza.event.core.PersistentType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Entity {

	//private EventHeader eventHeader;
	private PersistentType type;
	private EntityType entityType;
	private String entity;
	private String key;
	private Serializable keyValue;
	//private Long revision;
}

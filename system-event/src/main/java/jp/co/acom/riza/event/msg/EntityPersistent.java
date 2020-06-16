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
public class EntityPersistent {

	private EventHeader eventHeader;
	private PersistentType persitenceEventType;
	private EntityType entityType;
	private String entityClassName;
	private String keyClassName;
	private Serializable keyObject;
	private Long revision;
}

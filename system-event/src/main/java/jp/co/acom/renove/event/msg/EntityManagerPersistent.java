package jp.co.acom.renove.event.msg;

import java.util.List;

import lombok.Data;

@Data
public class EntityManagerPersistent {
	private String entityManagerName;
	private String flowId;
	private List<EntityPersistent> entityPersistences;
}

package jp.co.acom.renove.event.msg;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class FlowEvent {
	private EventHeader eventHeader;
	private String flowId;
	private List<EntityManagerPersistent> entityManagerPersistences;
	
	public List<EntityPersistent> getEntityPersistence(String entityClassName) {
		List<EntityPersistent> list = new ArrayList<EntityPersistent>();
		for (EntityManagerPersistent emp : entityManagerPersistences) {
			for (EntityPersistent per : emp.getEntityPersistences()) {
				if (entityClassName.equals(per.getEntityClassName())) {
					list.add(per);
				}
			}
		}
		return list;
	}
	
}

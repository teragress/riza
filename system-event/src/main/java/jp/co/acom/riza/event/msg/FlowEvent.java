package jp.co.acom.riza.event.msg;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FlowEvent {
	private EventHeader eventHeader;
	private String flowId;
	private List<PersistentManager> entityManagerPersistences;

	public List<PersistentEntity> getEntityPersistence(String entityClassName) {
		List<PersistentEntity> list = new ArrayList<PersistentEntity>();
		for (PersistentManager emp : entityManagerPersistences) {
			for (PersistentEntity per : emp.getEntityPersistences()) {
				if (entityClassName.equals(per.getEntityClassName())) {
					list.add(per);
				}
			}
		}
		return list;
	}

}

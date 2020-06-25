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
	private Header header;
	private String flowId;
	private List<Manager> managers;

	public List<Entity> getEntityPersistence(String entityClassName) {
		List<Entity> list = new ArrayList<Entity>();
		for (Manager emp : managers) {
			for (Entity per : emp.getEntitys()) {
				if (entityClassName.equals(per.getEntity())) {
					list.add(per);
				}
			}
		}
		return list;
	}

}

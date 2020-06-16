package jp.co.acom.renove.event.msg;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.databind.ObjectMapper;

import jp.co.acom.riza.event.core.PersistentType;
import jp.co.acom.riza.event.msg.EntityManagerPersistent;
import jp.co.acom.riza.event.msg.EntityPersistence;
import jp.co.acom.riza.event.msg.EntityPersistent;
import jp.co.acom.riza.event.msg.EventHeader;
import jp.co.acom.riza.event.msg.FlowEvent;

public class JsonTest {
    @JsonTypeInfo(use = Id.NAME)
		private static FlowEvent flowEvent;

	public static void main(String[] args) {

		EventHeader eh = new EventHeader();
		eh.setRequestId("requestId");
		eh.setUserId("userId");
		
		flowEvent = new FlowEvent();
		EntityPersistent ep1 = new EntityPersistent();
		ep1.setEntityClassName("com.example.eventnotify.customer.entity.MultiKeyEntity");
		ep1.setEntityType(EntityPersistent.EntityType.RSOURCE);
		ep1.setKeyClassName("com.example.eventnotify.customer.entity.MultiKey");
		ep1.setKeyObject("{\"key1\":\"keyString\",\"key2\":10}");
		ep1.setPersitenceEventType(PersistentType.UPDATE);
		EntityPersistent ep2 = new EntityPersistent();
		ep2.setEntityClassName("com.example.eventnotify.customer.entity.MultiKeyEntity");
		ep2.setEntityType(EntityPersistent.EntityType.RSOURCE);
		ep2.setKeyClassName("com.example.eventnotify.customer.entity.MultiKey");
		ep2.setKeyObject("{\"key1\":\"keyString\",\"key2\":10}");
		ep2.setSqlType(EntityPersistence.EntityPersistent.UPDATE);
		
		EntityManagerPersistent emp1 = new EntityManagerPersistent();
		emp1.setEntityManagerName("EntityManagerName");
		emp1.setFlowId("flowid");
		List<EntityPersistent> el1 = new ArrayList<EntityPersistent>();
		el1.add(ep1);
		el1.add(ep2);
		emp1.setEntityPersistences(el1);

		List<EntityManagerPersistent> empl1 = new ArrayList<EntityManagerPersistent>();
		empl1.add(emp1);
		
		flowEvent.setEntityManagerPersistences(empl1);
		flowEvent.setEventHeader(eh);
		flowEvent.setFlowId("flowId");

		ObjectMapper mapper = new ObjectMapper();
		try {
			System.out.println(mapper.writeValueAsString(flowEvent));

			Class c = Class.forName("com.example.eventnotify.customer.entity.MultiKey");
			Object o = mapper.readValue("{\"key1\":\"keyString\",\"key2\":10}", c);
			System.out.println(o.getClass());
			System.out.println(o);

			// TODO 自動生成されたメソッド・スタブ
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

}

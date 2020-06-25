package jp.co.acom.riza.event.msg;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PersistentManager {
	private String entityManagerName;
	private List<PersistentEntity> entityPersistences;
	private Long revison;
}

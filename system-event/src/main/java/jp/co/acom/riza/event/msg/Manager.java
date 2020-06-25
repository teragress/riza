package jp.co.acom.riza.event.msg;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Manager {
	private String manager;
	private List<Entity> entitys;
	private Long revison;
}

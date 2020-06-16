package jp.co.acom.riza.event.msg;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class EventHeader {

	private String reqeustId;
	private String userId;
}

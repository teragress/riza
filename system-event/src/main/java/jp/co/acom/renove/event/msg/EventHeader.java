package jp.co.acom.renove.event.msg;

import lombok.Data;

@Data
public class EventHeader {

	private String requestId;
	private String userId;
}

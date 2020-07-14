package jp.co.acom.riza.event.command.parm;

import lombok.Data;

@Data
public class EventCommandResponse {
	public enum RC {OK,NG};
	
	private RC rc;
	private String msg;
}

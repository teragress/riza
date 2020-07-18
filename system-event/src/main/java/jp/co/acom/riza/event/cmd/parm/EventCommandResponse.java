package jp.co.acom.riza.event.cmd.parm;

import lombok.Data;

@Data
public class EventCommandResponse {
	public enum RC {OK,NG};
	
	private RC rc;
	private String msg;
}

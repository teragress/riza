package jp.co.acom.riza.event.cmd.parm;

import lombok.Data;

@Data
public class EventRecoveryParm {
	private String dateTime;
	
	private String toDateTime;
	
	private String tranid;
	
}

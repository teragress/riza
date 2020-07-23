package jp.co.acom.riza.event.cmd.parm;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class EventRecoveryParm {
	private LocalDateTime dateTime;
	private LocalDateTime toDateTime;
	private String tranid;
	
}

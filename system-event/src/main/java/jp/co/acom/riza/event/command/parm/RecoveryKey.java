package jp.co.acom.riza.event.command.parm;

import java.util.Date;

import lombok.Data;

@Data
public class RecoveryKey {
	private Date date;
	private String tranid;
	
}

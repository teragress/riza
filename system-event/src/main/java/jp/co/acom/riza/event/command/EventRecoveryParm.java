package jp.co.acom.riza.event.command;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class EventRecoveryParm {
	private LocalDateTime fromDateTime;
	private LocalDateTime toDateTime;
	
}

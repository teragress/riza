package jp.co.acom.riza.event.command;

import java.time.LocalDate;

import lombok.Data;

@Data
public class CheckpointCleanParm {
	private LocalDate baseDate;
	private Integer keepDays;
}

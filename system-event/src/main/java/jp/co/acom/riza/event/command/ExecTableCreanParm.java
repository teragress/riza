package jp.co.acom.riza.event.command;

import java.time.LocalDate;

import lombok.Data;

@Data
public class ExecTableCreanParm {
	private LocalDate baseDate;
	private Integer keepDays;

}

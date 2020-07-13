package jp.co.acom.riza.event.command.parm;

import java.time.LocalDate;

import lombok.Data;

@Data
public class ExecTableCreanParm {
	private LocalDate baseDate;
	private Integer keepDays;

}

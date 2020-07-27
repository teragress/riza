package jp.co.acom.riza.event.cmd.parm;

import lombok.Data;

@Data
public class CheckpointCleanParm {
	private String baseDatetime;
	private Integer keepDays;
	private Integer deletionSplitCount;
}

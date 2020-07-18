package jp.co.acom.riza.event.cmd.parm;

import lombok.Data;

@Data
public class KafkaMessageInfo {
	private String topic;
	private Long offset;
	private Integer partition;
}

package jp.co.acom.riza.event.cmd.parm;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.Data;

@Data
public class KafkaRecoveryParm {
	List<KafkaMessageInfo> msgInfo = new ArrayList<KafkaMessageInfo>();
}

package jp.co.acom.riza.event.command;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.Data;

@Data
public class KafkaRecoveryParm {
	@JsonDeserialize(contentAs = ArrayList.class)
	List<KafkaMessageInfo> msgInfo;
}

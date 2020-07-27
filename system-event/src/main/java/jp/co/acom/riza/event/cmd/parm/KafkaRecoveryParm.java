package jp.co.acom.riza.event.cmd.parm;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

/**
 * KAFKAメッセージリカバリーコマンドパラメーター
 * @author teratani
 *
 */
@Data
public class KafkaRecoveryParm {
	List<KafkaMessageInfo> msgInfo = new ArrayList<KafkaMessageInfo>();
}

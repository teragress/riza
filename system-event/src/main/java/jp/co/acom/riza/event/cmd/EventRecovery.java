package jp.co.acom.riza.event.cmd;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import jp.co.acom.riza.event.cmd.parm.EventRecoveryParm;
import jp.co.acom.riza.event.entity.EventCheckpointEntity;
import jp.co.acom.riza.event.entity.EventCheckpointEntityKey;
import jp.co.acom.riza.event.msg.KafkaTopicMessage;
import jp.co.acom.riza.event.msg.TranEvent;
import jp.co.acom.riza.event.repository.EventCheckPointEntityRepository;
import jp.co.acom.riza.event.utils.StringUtil;

@Service
public class EventRecovery {

	@Autowired
	EventCheckPointEntityRepository checkPointRepository;

	public void keyRecovery(EventRecoveryParm parm) throws Exception {
	
		EventCheckpointEntityKey key = new EventCheckpointEntityKey();
		key.setTranId(parm.getTranid());
		LocalDateTime dateTime = parm.getDateTime();
		Timestamp timestamp = Timestamp.valueOf(dateTime);
		key.setDatetime(timestamp);
		Optional<EventCheckpointEntity> entity = checkPointRepository.findById(key);
		if (entity.isPresent()) {
			TranEvent tranEvent = StringUtil.stringToTranEventEventObject(entity.get().getEventMsg());
			if (tranEvent.getManagers().size() > 0) {
				
			}
			
		} else {
			
		}
		
		
	}
	
	private void recoveryMqMessage(List<KafkaTopicMessage> kafkaTopicMessage,byte[] messageIdPrefix) {
		
		
		
		
		
		
		
		
		
	}

	public void rangeRecovery(EventRecoveryParm parm) {

	}
}

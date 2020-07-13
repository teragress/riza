package jp.co.acom.riza.event.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.acom.riza.event.repository.EventCheckPointEntityRepository;

@Service
public class CheckPointClean {

	@Autowired
	EventCheckPointEntityRepository checkPointRepository;
	
	public void exec() {

	
		
		
	}
}

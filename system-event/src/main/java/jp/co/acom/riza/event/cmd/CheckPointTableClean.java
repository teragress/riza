package jp.co.acom.riza.event.cmd;

import java.util.List;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import jp.co.acom.riza.event.config.EventConfiguration;
import jp.co.acom.riza.event.entity.EventCheckpointEntity;
import jp.co.acom.riza.event.repository.EventCheckPointEntityRepository;

@Service
public class CheckPointTableClean {

	@Autowired
	EventCheckPointEntityRepository checkPointRepository;
	
	@Autowired
	ApplicationContext applicationContext;
	
	@Transactional
	public int cleanCheckPoint(String baseDateTime,int maxDelete) {
		EntityManager em = (EntityManager)applicationContext.getBean(EventConfiguration.ENTITY_MANAGER_NAME);

		
		List<EventCheckpointEntity> checkpointList = em
				.createNamedQuery(EventCheckpointEntity.FIND_BY_CLEAN, EventCheckpointEntity.class)
				.setParameter("dateTime", baseDateTime)
				.setMaxResults(maxDelete)
				.getResultList();
		
		if (checkpointList.size() > 0) {
			checkPointRepository.deleteInBatch(checkpointList);
		}
		
		return checkpointList.size();
	}
}

package jp.co.acom.riza.event.cmd;

import java.util.List;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import jp.co.acom.riza.event.config.EventConfiguration;
import jp.co.acom.riza.event.entity.TranExecCheckEntity;
import jp.co.acom.riza.event.repository.TranExecCheckEntityRepository;

@Service
public class ExecTableClean {

	@Autowired
	TranExecCheckEntityRepository tranExecCheckRepository;
	
	@Autowired
	ApplicationContext applicationContext;
	
	@Transactional
	public int cleanTranExec(String baseDatetime,int maxDelete) {
		EntityManager em = (EntityManager)applicationContext.getBean(EventConfiguration.ENTITY_MANAGER_NAME);

		
		List<TranExecCheckEntity> tranExecList = em
				.createNamedQuery(TranExecCheckEntity.FIND_BY_CLEAN, TranExecCheckEntity.class)
				.setParameter("baseDatetime", baseDatetime)
				.setMaxResults(maxDelete)
				.getResultList();
		
		if (tranExecList.size() > 0) {
			tranExecCheckRepository.deleteInBatch(tranExecList);
		}
		
		return tranExecList.size();
	}
}

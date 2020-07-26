package jp.co.acom.riza.event.persist;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.co.acom.riza.system.utils.log.Logger;
import lombok.Getter;
import lombok.Setter;

/**
 * 変更イベントを保持する
 * @author mtera1003
 *
 */
@Getter
@Setter
public class PersistentHolder implements PersistentEventNotifier {
	
	private static final Logger logger = Logger.getLogger(PersistentHolder.class);
	private List<EntityPersistent> events = new ArrayList<>();
	/**
	 * エンティティマネージャーBean名
	 */
	private String entityManagerBeanName;
	/**
	 * リビジョン番号
	 */
	private long revision = -1;
	
	public enum AuditStatus {
		INIT,
		AUDIT_ENTITY_ON,
		AUDIT_ENTITY_WRITE,
		COMPLETE
	}
	private AuditStatus auditStatus = AuditStatus.INIT;
	/**
	 * 
	 */
	private PostCommitPersistentNotifier postCommitPersistentEventNotifier;
	/**
	 * @param entityManagerBeanName
	 */
	public PersistentHolder(String entityManagerBeanName) {
		super();
		this.entityManagerBeanName = entityManagerBeanName;
	}
	/**
	 *　変更イベントを保存する
	 */
	@Override
	public void notify(EntityPersistent event) {
		if (event.getEntityType() == EntityType.RESOURCE && auditStatus == AuditStatus.INIT) {
			logger.debug(entityManagerBeanName + " auditStatus INIT to AUDIT_ENTITY_ON");
			auditStatus = AuditStatus.AUDIT_ENTITY_ON;
		}
		events.add(event);
	}
	/**
	 *　リビジョンを保存する
	 */
	@Override
	public void notify(Long revision) {
		if (auditStatus == AuditStatus.AUDIT_ENTITY_ON) {
			logger.debug(entityManagerBeanName + " auditStatus AUDIT_ENTITY_ON to AUDIT_ENTITY_WRITE");
			auditStatus = AuditStatus.AUDIT_ENTITY_WRITE;
		}
		this.revision = revision;
	}
	
	/**
	 * 保持しているイベントを取得する.
	 *
	 * @return イベントのリスト
	 */
	public List<EntityPersistent> getEvents() {
		List<EntityPersistent> normalized = getNormalizedEvents();
		return normalized;
	}

	/**
	 * トランザクション内で発生したイベントを正規化する.
	 *
	 * <p>１つのトランザクション内で同じデータに対する変更が複数あるものを１つのイベントに集約する.
	 *
	 * @return 正規化したイベントの一覧
	 */
	private List<EntityPersistent> getNormalizedEvents() {
		Map<Serializable, List<EntityPersistent>> eventMap = new HashMap<>();
		events.forEach(
				it -> eventMap.computeIfAbsent(it.getEntityId(), key -> new ArrayList<>()).add(it));

		List<EntityPersistent> normalized = new ArrayList<>();
		for (List<EntityPersistent> sameIdEvents : eventMap.values()) {
			EntityPersistent first = sameIdEvents.get(0);
			EntityPersistent last = sameIdEvents.get(sameIdEvents.size() - 1);

			if (first.getPersistentType() == PersistentType.INSERT) {
				if (last.getPersistentType() != PersistentType.DELETE) {
					last.setPersistentType(PersistentType.INSERT);
					normalized.add(last);
				}
			} else {
				if (last.getPersistentType() == PersistentType.INSERT) {
					last.setPersistentType(PersistentType.UPDATE);
				}
				normalized.add(last);
			}
		}
		return normalized;
	}
	@Override
	public void notify(AuditStatus auditStatus) {
		this.auditStatus = auditStatus;
	}
}

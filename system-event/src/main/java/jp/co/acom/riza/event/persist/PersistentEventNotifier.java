package jp.co.acom.riza.event.persist;

import jp.co.acom.riza.event.persist.PersistentHolder.AuditStatus;

/**
 * {@link EntityPersistent} の通知を行う.
 */
public interface PersistentEventNotifier {
	void notify(EntityPersistent event);

	void notify(Long revision);
	
	void notify(AuditStatus auditStatus);
	
}

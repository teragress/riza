package jp.co.acom.riza.event.core;

import jp.co.acom.riza.event.core.PersistentHolder.AuditStatus;

/**
 * {@link EntityPersistent} の通知を行う.
 */
public interface PersistentEventNotifier {
	void notify(EntityPersistent event);

	void notify(Long revision);
	
	void notify(AuditStatus auditStatus);
	
}

package jp.co.acom.riza.event.persist;

import jp.co.acom.riza.event.persist.PersistentHolder.AuditStatus;

/**
 * エンティティマネージャー単位のhibernateインターセプターの通知を行うインターフェース
 */
public interface PersistentEventNotifier {
	
	/**
	 * パーシステント通知
	 * @param event
	 */
	void notify(EntityPersistent event,String businessProcess);

	/**
	 * リビジョン番号通知
	 * @param revision
	 */
	void notify(Long revision);
	
	/**
	 * 監査レコードイベント
	 * @param auditStatus
	 */
	void notify(AuditStatus auditStatus);
	
}

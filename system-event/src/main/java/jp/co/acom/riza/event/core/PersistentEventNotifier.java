package jp.co.acom.riza.event.core;

/**
 * {@link EntityPersistent} の通知を行う.
 */
public interface PersistentEventNotifier {
	void notify(EntityPersistent event);

	void notify(Long revision);
}

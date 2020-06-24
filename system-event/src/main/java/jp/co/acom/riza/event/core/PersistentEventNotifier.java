package jp.co.acom.riza.event.core;

/**
 * {@link PersistentEvent} の通知を行う.
 */
public interface PersistentEventNotifier {
	void notify(PersistentEvent event);

	void notify(Long revision);
}

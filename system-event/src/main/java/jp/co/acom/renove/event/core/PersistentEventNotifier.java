package jp.co.acom.renove.event.core;

/**
 * {@link PersistentEvent} の通知を行う.
 */
public interface PersistentEventNotifier {
  void notify(PersistentEvent event);
}

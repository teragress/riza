package jp.co.acom.renove.utils.event;

/**
 * {@link PersistentEvent} の通知を行う.
 */
public interface PersistentEventNotifier {
  void notify(PersistentEvent event);
}

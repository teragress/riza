package com.example.eventnotify.event;

/**
 * {@link PersistentEvent} の通知を行う.
 */
public interface PersistentEventNotifier {
  void notify(PersistentEvent event);
}

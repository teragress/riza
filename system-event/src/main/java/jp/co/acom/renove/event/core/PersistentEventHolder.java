package jp.co.acom.renove.event.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** 送信するイベントを保持する. */
public class PersistentEventHolder implements PersistentEventNotifier {
  private List<PersistentEvent> events = new ArrayList<>();

  @Override
  public void notify(PersistentEvent event) {
    events.add(event);
  }

  /**
   * 保持しているイベントを取得する.
   *
   * @return イベントのリスト
   */
  public List<PersistentEvent> getEvents() {
    List<PersistentEvent> normalized = getNormalizedEvents();
    return normalized;
  }

  /**
   * トランザクション内で発生したイベントを正規化する.
   *
   * <p>１つのトランザクション内で同じデータに対する変更が複数あるものを１つのイベントに集約する.
   *
   * @return 正規化したイベントの一覧
   */
  private List<PersistentEvent> getNormalizedEvents() {
    Map<Serializable, List<PersistentEvent>> eventMap = new HashMap<>();
    events.forEach(
        it -> eventMap.computeIfAbsent(it.getEntityId(), key -> new ArrayList<>()).add(it));

    List<PersistentEvent> normalized = new ArrayList<>();
    for (List<PersistentEvent> sameIdEvents : eventMap.values()) {
      PersistentEvent first = sameIdEvents.get(0);
      PersistentEvent last = sameIdEvents.get(sameIdEvents.size() - 1);

      if (first.getType() == PersistentType.INSERT) {
        if (last.getType() != PersistentType.DELETE) {
          last.setType(PersistentType.INSERT);
			normalized.add(last);
		}
      } else {
        if (last.getType() == PersistentType.INSERT) {
          last.setType(PersistentType.UPDATE);
        }
        normalized.add(last);
      }
    }
    return normalized;
  }
}

package jp.co.acom.riza.event.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 変更イベントを保持する
 * @author mtera1003
 *
 */
@Getter
@Setter
@ToString
public class PersistentEventHolder implements PersistentEventNotifier {
	private List<PersistentEvent> events = new ArrayList<>();
	/**
	 * エンティティマネージャーBean名
	 */
	private String entityManagerBeanName;
	/**
	 * リビジョン番号
	 */
	private long revision;

	/**
	 * @param entityManagerBeanName
	 */
	public PersistentEventHolder(String entityManagerBeanName) {
		super();
		this.entityManagerBeanName = entityManagerBeanName;
	}
	/**
	 *　変更イベントを保存する
	 */
	@Override
	public void notify(PersistentEvent event) {
		events.add(event);
	}
	/**
	 *　リビジョンを保存する
	 */
	@Override
	public void notify(Long revision) {
		this.revision = revision;
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
}

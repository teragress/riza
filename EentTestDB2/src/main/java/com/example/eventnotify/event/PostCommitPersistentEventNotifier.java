package com.example.eventnotify.event;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/** トランザクション中のイベントを Commit 後に送信する. */
@Component
@Scope(value = "transaction", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class PostCommitPersistentEventNotifier {
  private static final Logger LOGGER =
      LoggerFactory.getLogger(PostCommitPersistentEventNotifier.class);

  private List<PersistentEventHolder> eventHolders = new ArrayList<>();

  @PostConstruct
  public void initialize() {
    TransactionSynchronizationManager.registerSynchronization(new TransactionListener());
  }

  /**
   * イベントを保持しているオブジェクトを追加する.
   *
   * @param holder イベント保持オブジェクト
   */
  public void addEventHolder(PersistentEventHolder holder) {
    eventHolders.add(holder);
  }

  /**
   * イベントの送信を行う.
   */
  protected void doNotifyEvents() {
    List<PersistentEvent> events =
        eventHolders.stream().flatMap(it -> it.getEvents().stream()).collect(Collectors.toList());

    // 実際にはここで Kafka にイベントを送信する
    for (PersistentEvent event : events) {
      LOGGER.info("イベント通知：{}", event);
      LOGGER.info("Entity Class Name=" + event.getEntity().getClass().getName());
    }
  }

  private class TransactionListener extends TransactionSynchronizationAdapter {
    @Override
    public void afterCommit() {
      LOGGER.info("*********afterCommit()*******");
      doNotifyEvents();
    }

	@Override
	public void beforeCommit(boolean readOnly) {
	      LOGGER.info("*********beforeCommit(" + readOnly + ")********");
		super.beforeCommit(readOnly);
	}

	@Override
	public void beforeCompletion() {
	      LOGGER.info("*********beforeCompletion()*******");
		super.beforeCompletion();
	}

	@Override
	public void afterCompletion(int status) {
	    LOGGER.info("*********afterCompletion(" + status + ")********");
		super.afterCompletion(status);
	}
    
  }
}

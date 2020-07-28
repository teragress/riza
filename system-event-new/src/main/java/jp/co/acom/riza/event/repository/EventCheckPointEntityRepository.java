package jp.co.acom.riza.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import jp.co.acom.riza.event.entity.EventCheckpointEntity;
import jp.co.acom.riza.event.entity.EventCheckpointEntityKey;

/**
 * イベントチェックポイントテーブル用リポジトリー定義
 * 
 * @author teratani
 *
 */
public interface EventCheckPointEntityRepository
		extends JpaRepository<EventCheckpointEntity, EventCheckpointEntityKey> {
}

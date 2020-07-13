package jp.co.acom.riza.event.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import jp.co.acom.riza.event.entity.EventCheckpointEntity;
import jp.co.acom.riza.event.entity.EventCheckpointEntityKey;

public interface EventCheckPointEntityRepository extends JpaRepository<EventCheckpointEntity, EventCheckpointEntityKey> {}

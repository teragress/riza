package jp.co.acom.riza.event.repository;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import jp.co.acom.riza.event.entity.EventCheckpointEntity;
import jp.co.acom.riza.event.entity.EventCheckpointEntityKey;

public interface EventCheckPointEntityRepository
		extends JpaRepository<EventCheckpointEntity, EventCheckpointEntityKey> {
	@Query("select u from EventCheckpointEntity u datetime >= ? "
			+ "order by datetime fetch first ? rows only")
	List<EventCheckpointEntity> findByDatetimeFirst(Timestamp datetime, Integer max);
	
	@Query("select u from EventCheckpointEntity u datetime > ? "
			+ "order by datetime fetch first ? rows only")
	List<EventCheckpointEntity> findByDatetimeNext(Timestamp datetime, Integer max);
}

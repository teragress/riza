package jp.co.acom.riza.event.repository;
import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import jp.co.acom.riza.event.entity.TranExecCheckEntity;

public interface TranExecCheckEntityRepository extends JpaRepository<TranExecCheckEntity, String> {
	@Query("select u from TranExecCheckEntity u datetime <= ? "
			+ "fetch first ? rows only")
	List<TranExecCheckEntity> findByDatetimeFirst(Timestamp datetime, Integer max);
}

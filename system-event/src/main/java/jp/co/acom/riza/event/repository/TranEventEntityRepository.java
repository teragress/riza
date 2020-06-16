package jp.co.acom.riza.event.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import jp.co.acom.riza.event.entity.TranEventEntity;
import jp.co.acom.riza.event.entity.TranEventEntityKey;

public interface TranEventEntityRepository extends JpaRepository<TranEventEntity, TranEventEntityKey> {}

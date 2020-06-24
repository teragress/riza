package jp.co.acom.example.eventnotify.customer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import jp.co.acom.example.eventnotify.customer.entity.MultiKey;
import jp.co.acom.example.eventnotify.customer.entity.MultiKeyEntity;

public interface MultiKeyEntityRepository extends JpaRepository<MultiKeyEntity, MultiKey> {}

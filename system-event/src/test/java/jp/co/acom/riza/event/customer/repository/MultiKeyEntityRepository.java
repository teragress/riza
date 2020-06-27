package jp.co.acom.riza.event.customer.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import jp.co.acom.riza.event.customer.entity.MultiKey;
import jp.co.acom.riza.event.customer.entity.MultiKeyEntity;

public interface MultiKeyEntityRepository extends JpaRepository<MultiKeyEntity, MultiKey> {}

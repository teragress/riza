package jp.co.acom.riza.event.trade.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import jp.co.acom.riza.event.trade.entity.Trade;

public interface TradeRepository extends JpaRepository<Trade, Long> {
  List<Trade> findByCustomerId(long customerId);
}

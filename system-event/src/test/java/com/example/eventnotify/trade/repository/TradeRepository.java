package com.example.eventnotify.trade.repository;

import com.example.eventnotify.trade.entity.Trade;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TradeRepository extends JpaRepository<Trade, Long> {
  List<Trade> findByCustomerId(long customerId);
}

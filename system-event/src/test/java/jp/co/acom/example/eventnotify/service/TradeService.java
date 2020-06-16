package jp.co.acom.example.eventnotify.service;

import jp.co.acom.example.eventnotify.customer.entity.Customer;
import jp.co.acom.example.eventnotify.customer.repository.CustomerRepository;
import jp.co.acom.example.eventnotify.trade.entity.Trade;
import jp.co.acom.example.eventnotify.trade.repository.TradeRepository;
import jp.com.acom.example.eventnotify.service.logic.CustomerRankUpdater;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TradeService {
  private static final Logger LOGGER = LoggerFactory.getLogger(TradeService.class);

  @Autowired private CustomerRankUpdater customerRankUpdater;
  @Autowired private CustomerRepository customerRepository;
  @Autowired private TradeRepository tradeRepository;

  public void save(Trade trade) {
    LOGGER.info("start save.");
    Optional<Customer> customer = customerRepository.findById(trade.getCustomerId());
    if (!customer.isPresent()) {
      throw new RuntimeException("customer id " + trade.getCustomerId() + " does not exist.");
    }

    tradeRepository.save(trade);
    customerRankUpdater.updateRank(customer.get());
    LOGGER.info("end save.");
  }

  public List<Trade> getAll() {
    return tradeRepository.findAll();
  }

  public List<Trade> getByCustomer(long customerId) {
    return tradeRepository.findByCustomerId(customerId);
  }
}

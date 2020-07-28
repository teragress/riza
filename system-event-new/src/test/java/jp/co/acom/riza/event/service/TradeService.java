package jp.co.acom.riza.event.service;

import jp.co.acom.riza.event.customer.entity.Customer;
import jp.co.acom.riza.event.customer.repository.CustomerRepository;
import jp.co.acom.riza.event.trade.entity.Trade;
import jp.co.acom.riza.event.trade.repository.TradeRepository;

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

  @Autowired private jp.co.acom.riza.event.service.logic.CustomerRankUpdater customerRankUpdater;
  @Autowired private CustomerRepository customerRepository;
  @Autowired private TradeRepository tradeRepository;
  @Autowired private CommonContextInit init;


  public void save(Trade trade) {
    LOGGER.info("start save.");
    init.initCommonContxt();
    Optional<Customer> customer = customerRepository.findById(trade.getCustomerId());
    if (!customer.isPresent()) {
      throw new RuntimeException("customer id " + trade.getCustomerId() + " does not exist.");
    }

    tradeRepository.save(trade);
    customerRankUpdater.updateRank(customer.get());
    LOGGER.info("end save.");
  }

  public List<Trade> getAll() {
	    init.initCommonContxt();
    return tradeRepository.findAll();
  }

  public List<Trade> getByCustomer(long customerId) {
	    init.initCommonContxt();
    return tradeRepository.findByCustomerId(customerId);
  }
}

package jp.co.acom.example.eventnotify.service;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.acom.example.eventnotify.customer.entity.Customer;
import jp.co.acom.example.eventnotify.customer.entity.MultiKey;
import jp.co.acom.example.eventnotify.customer.entity.MultiKeyEntity;
import jp.co.acom.example.eventnotify.customer.repository.CustomerRepository;
import jp.co.acom.example.eventnotify.customer.repository.MultiKeyEntityRepository;
import jp.co.acom.example.eventnotify.service.logic.CustomerRankUpdater;
import jp.co.acom.example.eventnotify.trade.entity.Trade;
import jp.co.acom.example.eventnotify.trade.repository.TradeRepository;

@Service
@Transactional
public class CustomerService {
  private static final Logger LOGGER = LoggerFactory.getLogger(CustomerService.class);

  @Autowired private CustomerRepository customerRepository;
  @Autowired private MultiKeyEntityRepository multiKeyEntityRepository;
  @Autowired private TradeRepository tradeRepository;
  @Autowired private CommonContextInit init;

  public void save(Customer customer) {
    LOGGER.info("start save.");
    init.initCommonContxt();
    customerRepository.save(customer);
    customerRepository.flush();
    MultiKey mKey = new MultiKey();
    mKey.setKey1("key1");
    mKey.setKey2(1);
    MultiKeyEntity mEntity = new MultiKeyEntity();
    mEntity.setMultiKey(mKey);
    mEntity.setName("name");
    mEntity.setRank(1);
    multiKeyEntityRepository.save(mEntity);
    multiKeyEntityRepository.flush();
    Trade trade = new Trade();
    trade.setCustomerId((long)1);
    trade.setTotal(100);
    tradeRepository.save(trade);
    tradeRepository.flush();
   
    if ("ERROR".equals(customer.getName())) {
      // ロールバックテスト用
      throw new RuntimeException("ERROR on save");
    }
    LOGGER.info("end save.");
  }

  public void delete(long id) {
    LOGGER.info("start delete.");
    init.initCommonContxt();
    customerRepository.deleteById(id);
    LOGGER.info("end delete.");
  }

  public List<Customer> findAll() {
	    init.initCommonContxt();
    return customerRepository.findAll();
  }

  public void modify(Customer customer) {
    LOGGER.info("start modify.");
    init.initCommonContxt();
    customerRepository
        .findById(customer.getId())
        .ifPresent(
            target -> {
              target.setName(customer.getName());
              target.setRank(customer.getRank());
            });
    LOGGER.info("end modify.");
  }
}

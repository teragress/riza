package jp.co.acom.riza.event.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.acom.riza.event.customer.entity.Customer;
import jp.co.acom.riza.event.customer.entity.MultiKey;
import jp.co.acom.riza.event.customer.entity.MultiKeyEntity;
import jp.co.acom.riza.event.customer.repository.CustomerRepository;
import jp.co.acom.riza.event.customer.repository.MultiKeyEntityRepository;
import jp.co.acom.riza.event.kafka.MessageUtil;
import jp.co.acom.riza.event.loan.entity.Loan;
import jp.co.acom.riza.event.loan.repository.LoanRepository;
import jp.co.acom.riza.event.trade.entity.Trade;
import jp.co.acom.riza.event.trade.repository.TradeRepository;

@Service
//@Transactional
public class CustomerService {
	private static final Logger LOGGER = LoggerFactory.getLogger(CustomerService.class);

	@Autowired
	private CustomerRepository customerRepository;
	@Autowired
	private MultiKeyEntityRepository multiKeyEntityRepository;
	@Autowired
	private TradeRepository tradeRepository;
	@Autowired
	private LoanRepository loanRepository;
	@Autowired
	private CommonContextInit init;

	@Transactional(timeout = 3600)
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
		trade.setCustomerId((long) 1);
		multiKeyEntityRepository.save(mEntity);
		trade.setTotal(100);
		tradeRepository.save(trade);
		tradeRepository.flush();
		MessageUtil.send("PRT_QUEUE", "test message 01");
		MessageUtil.send("PRT_QUEUE", "test message 02");
		Loan loan = new Loan();
		loan.setName("loan");
		loan.setRank(new Integer(10));
		loanRepository.save(loan);

		if ("ERROR".equals(customer.getName())) {
			// ロールバックテスト用
			throw new RuntimeException("ERROR on save");
		}
		LOGGER.info("end save.");
	}

	@Transactional
	public void delete(long id) {
		LOGGER.info("start delete.");
		init.initCommonContxt();
		customerRepository.deleteById(id);
		LOGGER.info("end delete.");
	}

	@Transactional
	public List<Customer> findAll() {
		init.initCommonContxt();
		for (Customer customer: customerRepository.findAll()) {
			System.out.println("*********************** "
					+ "customer=" + customer);
		}
			
		
		return customerRepository.findAll();
	}
	
	@Transactional(timeout = 3600)
	public Customer findAndMqput(Customer customer) {
		init.initCommonContxt();
		Long key = new Long(1);
		Optional<Customer> findCustomer = customerRepository.findById(key);
		System.out.println(findCustomer.get().toString());
		MessageUtil.send("PRT_QUEUE", "test message 03");
		MessageUtil.send("PRT_QUEUE", "test message 04");
		return findCustomer.get();
	}


	@Transactional(timeout = 3600)
	public void modify(Customer customer) {
		LOGGER.info("start modify.");
		init.initCommonContxt();
		customerRepository.findById(customer.getId()).ifPresent(target -> {
			target.setName(customer.getName());
			target.setRank(customer.getRank());
		});
		LOGGER.info("end modify.");
	}
}

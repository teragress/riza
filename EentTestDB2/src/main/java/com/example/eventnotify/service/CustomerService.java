package com.example.eventnotify.service;

import com.example.eventnotify.customer.entity.Customer;
import com.example.eventnotify.customer.entity.MultiKey;
import com.example.eventnotify.customer.entity.MultiKeyEntity;
import com.example.eventnotify.customer.repository.CustomerRepository;
import com.example.eventnotify.customer.repository.MultiKeyEntityRepository;

import java.util.List;

import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CustomerService {
  private static final Logger LOGGER = LoggerFactory.getLogger(CustomerService.class);

  @Autowired private CustomerRepository customerRepository;
  @Autowired private MultiKeyEntityRepository multiKeyEntityRepository;
  @Autowired private ApplicationContext appContext;

  public void save(Customer customer) {
    LOGGER.info("start save.");
	/*
	 * String[] beans = appContext.getBeanDefinitionNames(); for (String bean:beans
	 * ) { System.out.println("bean=" + bean); }
	 */
    
    EntityManager em = (EntityManager) appContext.getBean("customerEntityManager");
    System.out.println("em=" + em);
    
    MultiKeyEntity multiKeyEntity = new MultiKeyEntity();
    MultiKey multiKey = new MultiKey();
    multiKey.setKey1("keyString");
    multiKey.setKey2(10);
    multiKeyEntity.setMultiKey(multiKey);
    multiKeyEntity.setName("name");
    multiKeyEntity.setRank(20);
    multiKeyEntityRepository.save(multiKeyEntity);
    
    customerRepository.save(customer);

    if ("ERROR".equals(customer.getName())) {
      // ロールバックテスト用
      throw new RuntimeException("ERROR on save");
    }
    LOGGER.info("end save.");
  }

  public void delete(long id) {
    LOGGER.info("start delete.");
    customerRepository.deleteById(id);
    LOGGER.info("end delete.");
  }

  public List<Customer> findAll() {
    return customerRepository.findAll();
  }

  public void modify(Customer customer) {
    LOGGER.info("start modify.");
    MultiKey multiKey = new MultiKey();
    multiKey.setKey1("keyString");
    multiKey.setKey2(10);
    multiKeyEntityRepository
        .findById(multiKey)
        .ifPresent(
            target -> {
              target.setName(customer.getName());
              target.setRank(customer.getRank());
            });
    LOGGER.info("end modify.");
  }
}

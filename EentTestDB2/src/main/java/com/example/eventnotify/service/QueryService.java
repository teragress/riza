package com.example.eventnotify.service;

import com.example.eventnotify.customer.entity.Customer;
import com.example.eventnotify.customer.entity.MultiKey;
import com.example.eventnotify.customer.entity.MultiKeyEntity;
import com.example.eventnotify.customer.repository.CustomerRepository;
import com.example.eventnotify.customer.repository.MultiKeyEntityRepository;
import com.example.eventnotify.event.ObjectConverter;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;

import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.query.AuditQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.query.Jpa21Utils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class QueryService {
  private static final Logger LOGGER = LoggerFactory.getLogger(QueryService.class);
  @Autowired private ApplicationContext appContext;

  public void queryTest01() throws ClassNotFoundException {
    LOGGER.info("start save.");
	/*
	 * String[] beans = appContext.getBeanDefinitionNames(); for (String bean:beans
	 * ) { System.out.println("bean=" + bean); }
	 */
    
    EntityManager em = (EntityManager) appContext.getBean("customerEntityManager");
    AuditReader auditReader = AuditReaderFactory.get(em);
    Class entityClass = Class.forName("com.example.eventnotify.customer.entity.MultiKeyEntity"); 
    //Serializable key = "MultiKey(key1=keyString, key2=10)";
    Serializable key = "1";
    MultiKey multiKey = new MultiKey("keyString",10);
    Object keyObject = ObjectConverter.deserializeObject("rO0ABXNyADBjb20uZXhhbXBsZS5ldmVudG5vdGlmeS5jdXN0b21lci5lbnRpdHkuTXVsdGlLZXnikEb1y9HmQgIAAkwABGtleTF0ABJMamF2YS9sYW5nL1N0cmluZztMAARrZXkydAATTGphdmEvbGFuZy9JbnRlZ2VyO3hwdAAJa2V5U3RyaW5nc3IAEWphdmEubGFuZy5JbnRlZ2VyEuKgpPeBhzgCAAFJAAV2YWx1ZXhyABBqYXZhLmxhbmcuTnVtYmVyhqyVHQuU4IsCAAB4cAAAAAo=");
    
    AuditQuery auditQuery = auditReader.createQuery()
    .forRevisionsOfEntity(entityClass, false)
    .addProjection(AuditEntity.revisionNumber())
    .add(
        AuditEntity.and(
           AuditEntity.id().eq(keyObject),
           AuditEntity.property("version").le((long)10)))
    .addOrder(AuditEntity.revisionNumber().desc())
    .setMaxResults(2);
    List<Number> result =  auditQuery.getResultList();
    for (Number num: result) {
    	LOGGER.info("result number=" + num);
    }
    

    LOGGER.info("end save.");
  }

}

package com.example.eventnotify.customer;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.support.SharedEntityManagerBean;

import jp.co.acom.riza.event.core.PersistentEventHolder;
import jp.co.acom.riza.event.core.PersistentEventInterceptor;
import jp.co.acom.riza.event.core.PersistentEventNotifier;
import jp.co.acom.riza.event.core.PostCommitPersistentEventNotifier;

/** customer パッケージ用の EntityManager の設定. */
@EnableJpaRepositories(
    entityManagerFactoryRef = "customerEntityManagerFactory",
    basePackages = "com.example.eventnotify.customer.repository")
@Configuration
public class CustomerJpaConfiguration {
  // 対象の Entity が存在するパッケージ.
  private static final String ENTITY_PACKAGE = "com.example.eventnotify.customer.entity";
  private static final String ENTITY_MANAGER_FACTORY = "customerEntityManagerFactory";
  private static final String ENTITY_MANAGER_BEAN_NAME = "customerEntityManager";

  @Autowired protected PostCommitPersistentEventNotifier postCommitPersistentEventNotifier;

  @Autowired protected PersistentEventNotifier customerPersistentEventNotifier;

  /**
   * customerパッケージ用の EntityManagerFactory の定義.
   *
   * @param dataSource データソース
   * @param builder EntityMangerFactoryBuilder
   * @return
   */
  @Bean
  @Primary
  public LocalContainerEntityManagerFactoryBean customerEntityManagerFactory(
      DataSource dataSource, EntityManagerFactoryBuilder builder) {
    Map<String, Object> properties = new HashMap<>();
    // HibernateのInterceptorを設定する
    properties.put("hibernate.ejb.interceptor", customerPersistentEventInterceptor());

    return builder
        .dataSource(dataSource)
        .packages(ENTITY_PACKAGE)
        .jta(true)
        .properties(properties)
        .build();
  }

  /**
   * customerパッケージ用のEntityManager を直接 DI できるようにする定義.
   *
   * @param customerEntityManagerFactory EntityManager の Factory
   * @return
   */
  @Bean
  public SharedEntityManagerBean customerEntityManager(
      @Qualifier(ENTITY_MANAGER_FACTORY)
          EntityManagerFactory customerEntityManagerFactory) {
    SharedEntityManagerBean sharedEntityManagerBean = new SharedEntityManagerBean();
    sharedEntityManagerBean.setEntityManagerFactory(customerEntityManagerFactory);
    return sharedEntityManagerBean;
  }

  /**
   * Hibernate の Interceptor.
   *
   * @return
   */
  @Bean
  public PersistentEventInterceptor customerPersistentEventInterceptor() {
    PersistentEventInterceptor interceptor = new PersistentEventInterceptor();
    interceptor.setEventNotifier(customerPersistentEventNotifier);
    interceptor.setEntityPackage(ENTITY_PACKAGE);
    return interceptor;
  }

  /**
   * customerパッケージのエンティティの変更を保持するオブジェクトの定義.
   *
   * @param customerEntityManager customerパッケージのEntityManager
   * @return
   */
  @Bean
  @Scope(value = "transaction", proxyMode = ScopedProxyMode.INTERFACES)
  public jp.co.acom.riza.event.core.PersistentEventNotifier customerPersistentEventNotifier(
      @Qualifier(ENTITY_MANAGER_BEAN_NAME) EntityManager customerEntityManager) {
    PersistentEventHolder holder = new PersistentEventHolder(ENTITY_MANAGER_BEAN_NAME);
    postCommitPersistentEventNotifier.addEventHolder(holder);
    return holder;
  }
}

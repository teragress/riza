package jp.co.acom.riza.event.customer;

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

import jp.co.acom.riza.event.core.PersistentHolder;
import jp.co.acom.riza.event.core.PersistentInterceptor;
import jp.co.acom.riza.event.core.PersistentEventNotifier;
import jp.co.acom.riza.event.core.PostCommitPersistentNotifier;

/** customer パッケージ用の EntityManager の設定. */
@EnableJpaRepositories(
    entityManagerFactoryRef = "customerEntityManagerFactory",
    basePackages = "jp.co.acom.riza.event.customer.repository")
@Configuration
public class CustomerJpaConfiguration {
  // 対象の Entity が存在するパッケージ.
  private static final String ENTITY_PACKAGE = "jp.co.acom.riza.event.customer.entity";
  private static final String ENTITY_MANAGER_FACTORY = "customerEntityManagerFactory";
  private static final String ENTITY_MANAGER_BEAN_NAME = "customerEntityManager";

  @Autowired protected PostCommitPersistentNotifier postCommitPersistentEventNotifier;

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
  public PersistentInterceptor customerPersistentEventInterceptor() {
    PersistentInterceptor interceptor = new PersistentInterceptor();
    interceptor.setEventNotifier(customerPersistentEventNotifier);
    interceptor.setEntityPackage(ENTITY_PACKAGE);
     interceptor.setPostNotifier(postCommitPersistentEventNotifier);
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
  public PersistentEventNotifier customerPersistentEventNotifier(
      @Qualifier(ENTITY_MANAGER_BEAN_NAME) EntityManager customerEntityManager) {
    PersistentHolder holder = new PersistentHolder(ENTITY_MANAGER_BEAN_NAME);
    holder.setPostCommitPersistentEventNotifier(postCommitPersistentEventNotifier);
    postCommitPersistentEventNotifier.addEventHolder(holder);
    return holder;
  }
}

package jp.co.acom.example.eventnotify.trade;

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
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.support.SharedEntityManagerBean;

import jp.co.acom.riza.event.core.PersistentEventHolder;
import jp.co.acom.riza.event.core.PersistentEventInterceptor;
import jp.co.acom.riza.event.core.PersistentEventNotifier;
import jp.co.acom.riza.event.core.PostCommitPersistentEventNotifier;

/** trade パッケージ用の EntityManager の設定. */
@EnableJpaRepositories(
    entityManagerFactoryRef = "tradeEntityManagerFactory",
    basePackages = "jp.co.acom.example.eventnotify.trade.repository")
@Configuration
public class TradeJpaConfiguration {
  // 対象の Entity が存在するパッケージ.
  private static final String ENTITY_PACKAGE = "jp.co.acom.example.eventnotify.trade.entity";
  private static final String ENTITY_MANAGER_FACTORY = "tradeEntityManagerFactory";
  private static final String ENTITY_MANAGER_BEAN_NAME = "tradeEntityManager";
  
  @Autowired protected PostCommitPersistentEventNotifier postCommitPersistentEventNotifier;

  @Autowired protected PersistentEventNotifier tradePersistentEventNotifier;

  /**
   * trade パッケージ用の EntityManagerFactory の定義.
   *
   * @param dataSource データソース
   * @param builder EntityMangerFactoryBuilder
   * @return
   */
  @Bean
  public LocalContainerEntityManagerFactoryBean tradeEntityManagerFactory(
      DataSource dataSource, EntityManagerFactoryBuilder builder) {
    Map<String, Object> properties = new HashMap<>();
    // HibernateのInterceptorを設定する
    properties.put("hibernate.ejb.interceptor", tradePersistentEventInterceptor());

    return builder
        .dataSource(dataSource)
        .packages(ENTITY_PACKAGE)
        .jta(true)
        .properties(properties)
        .build();
  }

  /**
   * trade パッケージ用のEntityManager を直接 DI できるようにする定義.
   *
   * @param tradeEntityManagerFactory EntityManager の Factory
   * @return
   */
  @Bean
  public SharedEntityManagerBean tradeEntityManager(
      @Qualifier(ENTITY_MANAGER_FACTORY) EntityManagerFactory tradeEntityManagerFactory) {
    SharedEntityManagerBean sharedEntityManagerBean = new SharedEntityManagerBean();
    sharedEntityManagerBean.setEntityManagerFactory(tradeEntityManagerFactory);
    return sharedEntityManagerBean;
  }

  /**
   * Hibernate の Interceptor.
   *
   * @return
   */
  @Bean
  public PersistentEventInterceptor tradePersistentEventInterceptor() {
    
    PersistentEventInterceptor interceptor = new PersistentEventInterceptor();
    interceptor.setEventNotifier(tradePersistentEventNotifier);
    interceptor.setEntityPackage(ENTITY_PACKAGE);
    return interceptor;
  }

  /**
   * trade パッケージのエンティティの変更を保持するオブジェクトの定義.
   *
   * @param tradeEntityManager customerパッケージのEntityManager
   * @return
   */
  @Bean
  @Scope(value = "transaction", proxyMode = ScopedProxyMode.INTERFACES)
  public PersistentEventNotifier tradePersistentEventNotifier(
      @Qualifier("tradeEntityManager") EntityManager tradeEntityManager) {
    PersistentEventHolder holder = new PersistentEventHolder(ENTITY_MANAGER_BEAN_NAME);
    postCommitPersistentEventNotifier.addEventHolder(holder);
    return holder;
  }
}

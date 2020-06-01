package jp.co.acom.renove.event.config;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.CustomScopeConfigurer;
import org.springframework.beans.factory.config.Scope;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.support.SharedEntityManagerBean;
import org.springframework.transaction.support.SimpleTransactionScope;

/** system-event パッケージ用の EntityManager の設定. */
@EnableJpaRepositories(
    entityManagerFactoryRef = "systemEntityManagerFactory",
    basePackages = "jp.co.acom.renove.event.repository")
@Configuration
public class EventConfiguration {
  // 対象の Entity が存在するパッケージ.
  private static final String ENTITY_PACKAGE = "jp.co.acom.renove.event.entity";

  @Bean
  public Scope transactionScope() {
    return new SimpleTransactionScope();
  }

  @Bean
  public CustomScopeConfigurer customScopeConfigurer() {
    CustomScopeConfigurer customScopeConfigurer = new CustomScopeConfigurer();
    customScopeConfigurer.addScope("transaction", transactionScope());
    return customScopeConfigurer;
  }
  /**
   * system パッケージ用の EntityManagerFactory の定義.
   *
   * @param dataSource データソース
   * @param builder EntityMangerFactoryBuilder
   * @return
   */
  @Bean
  public LocalContainerEntityManagerFactoryBean systemEntityManagerFactory(
      DataSource dataSource, EntityManagerFactoryBuilder builder) {
    //Map<String, Object> properties = new HashMap();
    // HibernateのInterceptorを設定する
    //properties.put("hibernate.ejb.interceptor", systemPersistentEventInterceptor());

    return builder
        .dataSource(dataSource)
        .packages(ENTITY_PACKAGE)
        .jta(true)
//        .properties(properties)
        .build();
  }

  /**
   * system パッケージ用のEntityManager を直接 DI できるようにする定義.
   *
   * @param systemEntityManagerFactory EntityManager の Factory
   * @return
   */
  @Bean
  public SharedEntityManagerBean systemEntityManager(
      @Qualifier("systemEntityManagerFactory") EntityManagerFactory systemEntityManagerFactory) {
    SharedEntityManagerBean sharedEntityManagerBean = new SharedEntityManagerBean();
    sharedEntityManagerBean.setEntityManagerFactory(systemEntityManagerFactory);
    return sharedEntityManagerBean;
  }
}

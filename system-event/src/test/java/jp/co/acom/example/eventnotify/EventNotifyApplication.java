package jp.co.acom.example.eventnotify;

import org.springframework.beans.factory.config.CustomScopeConfigurer;
import org.springframework.beans.factory.config.Scope;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.support.SimpleTransactionScope;

/** A spring-boot application. */
@SpringBootApplication
public class EventNotifyApplication {
  // must have a main method spring-boot can run
  public static void main(String[] args) {
    SpringApplication.run(EventNotifyApplication.class, args);
  }

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
}

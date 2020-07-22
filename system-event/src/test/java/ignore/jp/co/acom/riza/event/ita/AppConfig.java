package ignore.jp.co.acom.riza.event.ita;

import org.springframework.beans.factory.config.CustomScopeConfigurer;
import org.springframework.beans.factory.config.Scope;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.support.SimpleTransactionScope;

@Configuration
@ComponentScan("jp.co.acom.riza")
@PropertySources({
//	@PropertySource("classpath:message.properties"),
	@PropertySource("classpath:application.yml")
})
//@SpringBootApplication(exclude = {
//        DataSourceAutoConfiguration.class,
//        HibernateJpaAutoConfiguration.class,
//        JpaRepositoriesAutoConfiguration.class,
//        DataSourceTransactionManagerAutoConfiguration.class
//        
//})
@EnableAutoConfiguration
@EnableTransactionManagement
@EnableConfigurationProperties
public class AppConfig {

	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
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

//	@Bean
//	public DataSource dataSource() {
//		return DataSourceBuilder.create().url("jdbc:h2:mem:customer:H2").driverClassName("org.h2.Driver").username("sa")
//				.password("").build();
//	}
}

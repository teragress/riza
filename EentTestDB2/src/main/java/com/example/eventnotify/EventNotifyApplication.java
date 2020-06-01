package com.example.eventnotify;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.config.CustomScopeConfigurer;
import org.springframework.beans.factory.config.Scope;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
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

	@Bean
	public DataSource dataSource() {
		/*
		 * TransactionAwareDataSourceProxy dataSource = new
		 * TransactionAwareDataSourceProxy( DataSourceBuilder .create()
		 * .username("vagrant") .password("vagrant")
		 * .driverClassName("com.ibm.db2.jcc.DB2Driver")
		 * .url("jdbc:db2://localhost:50000/acomdb") );
		 */
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setDriverClassName("com.ibm.db2.jcc.DB2Driver");
		dataSource.setUrl("jdbc:db2://localhost:50000/acomdb");
		dataSource.setUsername("vagrant");
		dataSource.setPassword("vagrant");
		dataSource.setDefaultAutoCommit(true);
		DataSource returnSource = new TransactionAwareDataSourceProxy(dataSource);

		return returnSource;

//    return dataSource;
	}

}

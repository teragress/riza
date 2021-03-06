package jp.co.acom.riza.event.config;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.support.SharedEntityManagerBean;

/**
 * system_eventパッケージ用のEntityManagerの設定
 * @author teratani
 *
 */
@EnableJpaRepositories(entityManagerFactoryRef = "systemEntityManagerFactory", basePackages = "jp.co.acom.riza.event.repository")
@Configuration
public class EventConfiguration {
	// 当該エンティティマネージャーのエンティティが存在するパッケージ.
	private static final String ENTITY_PACKAGE = "jp.co.acom.riza.event.entity";
	public static final String ENTITY_MANAGER_NAME = "systemEntityManager";
	
	/**
	 * system パッケージ用の EntityManagerFactory の定義.
	 *
	 * @param dataSource データソース
	 * @param builder EntityMangerFactoryBuilder
	 * @return LocalContainerEntityManagerFactoryBean
	 */
	@Bean
	public LocalContainerEntityManagerFactoryBean systemEntityManagerFactory(
			DataSource dataSource, EntityManagerFactoryBuilder builder) {

		return builder
				.dataSource(dataSource)
				.packages(ENTITY_PACKAGE)
				.jta(true)
				.build();
	}

	/**
	 * system パッケージ用のEntityManager を直接 DI できるようにする定義.
	 *
	 * @param systemEntityManagerFactory EntityManager の Factory
	 * @return SharedEntityManagerBean
	 */
	@Bean
	public SharedEntityManagerBean systemEntityManager(
			@Qualifier("systemEntityManagerFactory") EntityManagerFactory systemEntityManagerFactory) {
		SharedEntityManagerBean sharedEntityManagerBean = new SharedEntityManagerBean();
		sharedEntityManagerBean.setEntityManagerFactory(systemEntityManagerFactory);
		return sharedEntityManagerBean;
	}
}

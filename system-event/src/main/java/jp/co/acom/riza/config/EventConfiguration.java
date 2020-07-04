package jp.co.acom.riza.config;

import javax.sql.DataSource;

import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

/**
 * system_eventパッケージ用のEntityManagerの設定
 * @author mtera1003
 *
 */
@EnableJpaRepositories(entityManagerFactoryRef = "systemEntityManagerFactory", basePackages = "jp.co.acom.riza.event.repository")
@Configuration
public class EventConfiguration {
	// 当該エンティティマネージャーのエンティティが存在するパッケージ.
	private static final String ENTITY_PACKAGE = "jp.co.acom.riza.event.entity";
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
	 * @return
	 */
//	@Bean
//	public SharedEntityManagerBean systemEntityManager(
//			@Qualifier("systemEntityManagerFactory") EntityManagerFactory systemEntityManagerFactory) {
//		SharedEntityManagerBean sharedEntityManagerBean = new SharedEntityManagerBean();
//		sharedEntityManagerBean.setEntityManagerFactory(systemEntityManagerFactory);
//		return sharedEntityManagerBean;
//	}
}

package jp.co.acom.riza.event.customer;

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

import jp.co.acom.riza.event.config.AbstractJpaConfiguration;
import jp.co.acom.riza.event.persist.PersistentEventNotifier;
import jp.co.acom.riza.event.persist.PersistentInterceptor;

/**
 * エンティティマネージャー定義の例
 * @author teratani
 *
 */
//****************************************************************************************
// 全体を通してエンティティマネージャーの識別子を決める。この例では"customer"となる。以降は以降は識別子として記載する。
// entityManagerFactoryRefの名前を識別子+"EntityManagerFactory"で定義する。
// basePackages に対象となるリポジトリのパッケージを指定する。複数ある場合はカンマで区切って指定する。
//****************************************************************************************
@EnableJpaRepositories(entityManagerFactoryRef = "customerEntityManagerFactory", 
		basePackages = {"jp.co.acom.riza.event.customer.repository","jp.co.acom.riza.event.loan.repository"})
@Configuration
//****************************************************************************************
// クラス名は識別子+"JpaConfiguration"とする。
//****************************************************************************************
public class CustomerJpaConfiguration extends AbstractJpaConfiguration {
	//****************************************************************************************
	// 上記basePackagesに指定したものをパッケージ毎に名前をつけて定義する
	//****************************************************************************************
	private static final String ENTITY_PACKAGE = "jp.co.acom.riza.event.customer.entity";
	private static final String ENTITY_PACKAGE2 = "jp.co.acom.riza.event.loan.entity";

	//****************************************************************************************
	// ENTITY_MANAGER_FACTORYの指定を識別子+"EntityManagerFactory"で定義する。
	//****************************************************************************************
	private static final String ENTITY_MANAGER_FACTORY = "customerEntityManagerFactory";

	//****************************************************************************************
	// ENTITY_MANAGER_BEAN_NAMEの指定を識別子+"EntityManager"で定義する。
	//****************************************************************************************
	private static final String ENTITY_MANAGER_BEAN_NAME = "customerEntityManager";

	@Autowired
	protected PersistentEventNotifier customerPersistentEventNotifier;

	/**
	 * EntityManagerFactory の定義.
	 *
	 * @param dataSource データソース
	 * @param builder    EntityMangerFactoryBuilder
	 * @return LocalContainerEntityManagerFactoryBean
	 */
	@Bean
	@Primary
	//****************************************************************************************
	// メソッド名を識別子+"EntityManagerFactory"で定義する。
	//****************************************************************************************
	public LocalContainerEntityManagerFactoryBean customerEntityManagerFactory(DataSource dataSource,
			EntityManagerFactoryBuilder builder) {

		//****************************************************************************************
		// super.entityManagerFactoryの第三引数は識別子+"PersistentEventInterceptor()"で定義する。
		// 第四引数以降は上記で指定したエンティティパッケージを定義して個数分引数として指定する。
		//****************************************************************************************
		return super.entityManagerFactory(dataSource, builder, customerPersistentEventInterceptor(),ENTITY_PACKAGE,ENTITY_PACKAGE2);
	}

	/**
	 * EntityManagerを直接 DI できるようにする定義.
	 * @param entityManagerFactory エンティティマネージャーファクトリー
	 * @return SharedEntityManagerBean
	 */
	@Bean
	//****************************************************************************************
	// メソッド名を識別子+"EntityManager"で定義する。
	//****************************************************************************************
	public SharedEntityManagerBean customerEntityManager(
			@Qualifier(ENTITY_MANAGER_FACTORY) EntityManagerFactory entityManagerFactory) {
		return super.entityManager(entityManagerFactory);
	}

	/**
	 * Hibernateのインターセプターを登録する
	 *
	 * @return PersistentInterceptor
	 */
	@Bean
	//****************************************************************************************
	// メソッド名を識別子+"PersistentEventInterceptor"で定義する。
	//****************************************************************************************
	public PersistentInterceptor customerPersistentEventInterceptor() {

		//****************************************************************************************
		// super.persistentEventInterceptorの第一引数は識別子+"PersistentEventNotifier"で定義する。
		// 第二引数以降は上記で指定したエンティティパッケージを定義して個数分引数として指定する。
		//****************************************************************************************
		return super.persistentEventInterceptor(customerPersistentEventNotifier, ENTITY_PACKAGE,ENTITY_PACKAGE2);
	}

	/**
	 * パッケージのエンティティの変更を保持するオブジェクトの定義.
	 * @param entityManager エンティティマネージャー
	 * @return PersistentEventNotifier
	 */
	@Bean
	@Scope(value = "transaction", proxyMode = ScopedProxyMode.INTERFACES)
	//****************************************************************************************
	// メソッド名を識別子+"PersistentEventNotifier"で定義する。
	//****************************************************************************************
	public PersistentEventNotifier customerPersistentEventNotifier(
			@Qualifier(ENTITY_MANAGER_BEAN_NAME) EntityManager entityManager) {
		return super.persistentEventNotifier(ENTITY_MANAGER_BEAN_NAME, entityManager);
	}
}

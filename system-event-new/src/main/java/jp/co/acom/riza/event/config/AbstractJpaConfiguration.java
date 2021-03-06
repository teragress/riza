package jp.co.acom.riza.event.config;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.support.SharedEntityManagerBean;

import jp.co.acom.riza.context.CommonContext;
import jp.co.acom.riza.event.kafka.AppRouteHolder;
import jp.co.acom.riza.event.persist.PersistentEventNotifier;
import jp.co.acom.riza.event.persist.PersistentHolder;
import jp.co.acom.riza.event.persist.PersistentInterceptor;
import jp.co.acom.riza.event.persist.PostCommitPersistentNotifier;

/**
 * エンティティマネージャーを定義する抽象クラス<br>
 * エンティティマネージャーを作成する場合は当該クラスを継承して作成すること。
 * 
 * @author teratani
 *
 */
public abstract class AbstractJpaConfiguration {

	@Autowired
	protected PostCommitPersistentNotifier postCommitPersistentEventNotifier;

	@Autowired
	CommonContext commonContext;

	@Autowired
	AppRouteHolder appRouteHolder;

	/**
	 * EntityManagerFactory の定義.
	 *
	 * @param dataSource データソース
	 * @param builder    EntityMangerFactoryBuilder
	 * @return
	 */
	protected LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource,
			EntityManagerFactoryBuilder builder, PersistentInterceptor persistentInterceptor, String... entityPackage) {

		Map<String, Object> properties = new HashMap<>();

		properties.put("hibernate.ejb.interceptor", persistentInterceptor);

		return builder.dataSource(dataSource).packages(entityPackage).jta(true).properties(properties).build();
	}

	/**
	 * EntityManager を直接 DI できるようにする定義.
	 *
	 * @param entityManagerFactory EntityManager の Factory
	 * @return
	 */
	protected SharedEntityManagerBean entityManager(EntityManagerFactory entityManagerFactory) {
		SharedEntityManagerBean sharedEntityManagerBean = new SharedEntityManagerBean();
		sharedEntityManagerBean.setEntityManagerFactory(entityManagerFactory);
		return sharedEntityManagerBean;
	}

	/**
	 * Hibernate の Interceptor.
	 * 
	 * @param PersistentEventNotifier
	 * @param ENTITY_PACKAGE
	 *
	 * @return
	 */
	protected PersistentInterceptor persistentEventInterceptor(PersistentEventNotifier customerPersistentEventNotifier,
			String... entityPackages) {
		PersistentInterceptor interceptor = new PersistentInterceptor();
		interceptor.setEventNotifier(customerPersistentEventNotifier);
		interceptor.setEntityPackages(entityPackages);
		interceptor.setPostNotifier(postCommitPersistentEventNotifier);
		return interceptor;
	}

	/**
	 * エンティティの変更を保持するオブジェクトの定義.
	 *
	 * @param EntityManager パッケージのEntityManager
	 * @return
	 */
	protected PersistentEventNotifier persistentEventNotifier(String entityManaerBeanName,
			EntityManager entityManager, String domainName) {
		PersistentHolder persistentHolder = new PersistentHolder(entityManaerBeanName);
		persistentHolder.setPostCommitPersistentEventNotifier(postCommitPersistentEventNotifier);
		persistentHolder.setDomainName(domainName);
		postCommitPersistentEventNotifier.addEventHolder(persistentHolder);
		return persistentHolder;
	}
}

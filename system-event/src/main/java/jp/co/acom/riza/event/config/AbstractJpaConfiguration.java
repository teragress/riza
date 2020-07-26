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

import jp.co.acom.riza.event.core.PersistentHolder;
import jp.co.acom.riza.event.core.PersistentInterceptor;
import jp.co.acom.riza.event.core.PersistentEventNotifier;
import jp.co.acom.riza.event.core.PostCommitPersistentNotifier;

/**
 * エンティティマネージャーを定義する抽象クラス<br>
 * エンティティマネージャーを作成する場合は当該クラスを継承して作成すること。
 * @author vagrant
 *
 */
public abstract class AbstractJpaConfiguration {

	@Autowired
	protected PostCommitPersistentNotifier postCommitPersistentEventNotifier;

	/**
	 * EntityManagerFactory の定義.
	 *
	 * @param dataSource データソース
	 * @param builder    EntityMangerFactoryBuilder
	 * @return
	 */
	protected LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource,
			EntityManagerFactoryBuilder builder,PersistentInterceptor persistentInterceptor,String... entityPackage) {

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
	protected PersistentEventNotifier persistentEventNotifier(String entityManaerBeanName, EntityManager entityManager) {
		System.out.println("********************add PersistentHolder*********************" + entityManaerBeanName);
		PersistentHolder holder = new PersistentHolder(entityManaerBeanName);
		holder.setPostCommitPersistentEventNotifier(postCommitPersistentEventNotifier);
		postCommitPersistentEventNotifier.addEventHolder(holder);
		return holder;
	}
}

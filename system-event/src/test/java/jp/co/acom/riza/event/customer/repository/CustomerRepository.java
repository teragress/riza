package jp.co.acom.riza.event.customer.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import jp.co.acom.riza.event.customer.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {}

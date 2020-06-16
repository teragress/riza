package jp.co.acom.example.eventnotify.customer.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import jp.co.acom.example.eventnotify.customer.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {}

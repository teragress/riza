package jp.co.acom.riza.event.loan.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import jp.co.acom.riza.event.loan.entity.Loan;

public interface LoanRepository extends JpaRepository<Loan, Long> {}

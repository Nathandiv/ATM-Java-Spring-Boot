package com.example.atm_app.repository;

import com.example.atm_app.entity.BankTransaction;
import jakarta.transaction.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import java.util.List;

public interface TransactionRepository extends JpaRepository<BankTransaction, Integer> {
    List<BankTransaction> findByAccountId(int accountId);
}

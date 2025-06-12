package com.example.atm_app.repository;

import com.example.atm_app.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Integer> {
    Optional<Account> findByPin(int pin);
    Optional<Account> findByName(String name);
}
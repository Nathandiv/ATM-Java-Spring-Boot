package com.example.atm_app.controller;

import com.example.atm_app.dto.LoginRequest;
import com.example.atm_app.entity.Account;
import com.example.atm_app.entity.BankTransaction;
import com.example.atm_app.service.AccountService;
import jakarta.transaction.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    // 🔐 Login using PIN
    public ResponseEntity<Account> login(@RequestBody LoginRequest loginRequest) {
        Optional<Account> account = accountService.login(loginRequest.getPin());
        return account.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(401).build());
    }
    // 💰 Get account balance
    @GetMapping("/balance/{accountId}")
    public ResponseEntity<?> getBalance(@PathVariable int accountId) {
        Optional<Float> balance = accountService.getBalance(accountId);
        return balance.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 💵 Deposit funds
    @PostMapping("/deposit")
    public ResponseEntity<String> deposit(
            @RequestParam int accountId,
            @RequestParam float amount) {
        return ResponseEntity.ok(accountService.deposit(accountId, amount));
    }

    // 💳 Withdraw funds
    @PostMapping("/withdraw")
    public ResponseEntity<String> withdraw(
            @RequestParam int accountId,
            @RequestParam float amount) {
        return ResponseEntity.ok(accountService.withdraw(accountId, amount));
    }

    // 💸 Transfer funds
    @PostMapping("/transfer")
    public ResponseEntity<String> transfer(
            @RequestParam int senderId,
            @RequestParam int recipientId,
            @RequestParam float amount) {
        return ResponseEntity.ok(accountService.transfer(senderId, recipientId, amount));
    }

    // 📄 View all transactions for an account
    @GetMapping("/transactions/{accountId}")
    public ResponseEntity<List<BankTransaction>> getTransactions(@PathVariable int accountId) {
        return ResponseEntity.ok(accountService.getTransactions(accountId));
    }
}

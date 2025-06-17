package com.example.atm_app.controller;

import com.example.atm_app.dto.AddBeneficiaryRequest;
import com.example.atm_app.dto.LoginRequest;
import com.example.atm_app.dto.RegisterRequest;
import com.example.atm_app.dto.WithdrawRequest;
import com.example.atm_app.entity.Account;
import com.example.atm_app.entity.BankTransaction;
import com.example.atm_app.entity.Beneficiary;
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

    @PostMapping("/register")
    public ResponseEntity<Account> register(@RequestBody RegisterRequest registerRequest) {
        Account created = accountService.register(registerRequest);
        return ResponseEntity.ok(created);
    }
    // get all accounts
    @GetMapping("/accounts")
    public ResponseEntity<List<Account>> getAllAccounts() {
        return ResponseEntity.ok(accountService.getAllAccounts());
    }


    // 🔐 Login using PIN
    @PostMapping("/login")
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
    public ResponseEntity<String> withdraw(@RequestBody WithdrawRequest request) {
        return ResponseEntity.ok(accountService.withdraw(request.getAccountId(), request.getAmount()));
    }

    // add beneficiaries
    @PostMapping("/beneficiaries")
    public ResponseEntity<String> addBeneficiary(@RequestBody AddBeneficiaryRequest request) {
        String response = accountService.addBeneficiary(request.getSenderId(), request.getRecipientId());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/beneficiaries")
    public ResponseEntity<List<Beneficiary>> getAllBeneficiaries() {
        List<Beneficiary> beneficiaries = accountService.getAllBeneficiaries();
        return ResponseEntity.ok(beneficiaries);
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

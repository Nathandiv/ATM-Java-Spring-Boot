package com.example.atm_app.service;

import com.example.atm_app.dto.RegisterRequest;
import com.example.atm_app.entity.Account;
import com.example.atm_app.entity.Beneficiary;
import com.example.atm_app.entity.BankTransaction;
import com.example.atm_app.repository.AccountRepository;
import com.example.atm_app.repository.BeneficiaryRepository;
import com.example.atm_app.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // ✅ Correct import

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepo;
    private final TransactionRepository transactionRepo;
    private final BeneficiaryRepository beneficiaryRepo;

    // 🔐 Login with PIN
    public Optional<Account> login(int pin) {
        return accountRepo.findByPin(pin);
    }

    // 💰 Get Balance by Account ID
    public Optional<Float> getBalance(int accountId) {
        Optional<Account> account = accountRepo.findById(accountId);
        if (account.isPresent()) {
            System.out.println("Account found: " + account.get().getName() + " | Balance: " + account.get().getBalance());
        } else {
            System.out.println("No account found with ID: " + accountId);
        }
        return account.map(Account::getBalance);
    }

    public Account register(RegisterRequest request) {
        Account account = Account.builder()
                .name(request.getName())
                .pin(request.getPin())
                .balance(request.getInitialBalance())
                .build();

        return accountRepo.save(account);
    }

    public List<Account> getAllAccounts() {
        return accountRepo.findAll();
    }


    public String addBeneficiary(int senderId, int recipientId) {
        boolean exists = beneficiaryRepo.findBySenderId(senderId).stream()
                .anyMatch(b -> b.getRecipientId().equals(recipientId));

        if (exists) {
            return "Beneficiary already exists.";
        }

        beneficiaryRepo.save(Beneficiary.builder()
                .senderId(senderId)
                .recipientId(recipientId)
                .build());

        return "Beneficiary added successfully.";
    }

    public List<Beneficiary> getAllBeneficiaries() {
        return beneficiaryRepo.findAll();
    }


    // 💵 Deposit Money
    @Transactional
    public String deposit(int accountId, float amount) {
        Account account = accountRepo.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        account.setBalance(account.getBalance() + amount);
        accountRepo.save(account);

        transactionRepo.save(BankTransaction.builder()
                .account(account)
                .type("Deposit")
                .amount(amount)
                .balanceAfter(account.getBalance())
                .timestamp(LocalDateTime.now())
                .build());

        return "Deposit successful. New balance: " + account.getBalance();
    }

    @Transactional
    public String transfer(int senderId, int recipientId, float amount) {
        Account sender = accountRepo.findById(senderId)
                .orElseThrow(() -> new RuntimeException("Sender not found"));
        Account recipient = accountRepo.findById(recipientId)
                .orElseThrow(() -> new RuntimeException("Recipient not found"));

        if (sender.getBalance() < amount) {
            return "Insufficient funds!";
        }

        sender.setBalance(sender.getBalance() - amount);
        recipient.setBalance(recipient.getBalance() + amount);

        accountRepo.save(sender);
        accountRepo.save(recipient);

        transactionRepo.save(BankTransaction.builder()
                .account(sender)
                .type("Transfer Sent to " + recipient.getName())
                .amount(amount)
                .balanceAfter(sender.getBalance())
                .timestamp(LocalDateTime.now())
                .build());

        transactionRepo.save(BankTransaction.builder()
                .account(recipient)
                .type("Transfer Received from " + sender.getName())
                .amount(amount)
                .balanceAfter(recipient.getBalance())
                .timestamp(LocalDateTime.now())
                .build());

        boolean isNew = beneficiaryRepo.findBySenderId(senderId).stream()
                .noneMatch(b -> b.getRecipientId().equals(recipientId));

        if (isNew) {
            beneficiaryRepo.save(Beneficiary.builder()
                    .senderId(senderId)
                    .recipientId(recipientId)
                    .build());
        }

        return "Transfer successful. New balance: " + sender.getBalance();
    }

    @Transactional
    public String withdraw(int accountId, float amount) {
        Account account = accountRepo.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        if (account.getBalance() < amount) {
            return "Insufficient funds!";
        }

        account.setBalance(account.getBalance() - amount);
        accountRepo.save(account);

        transactionRepo.save(BankTransaction.builder()
                .account(account)
                .type("Withdraw")
                .amount(amount)
                .balanceAfter(account.getBalance())
                .timestamp(LocalDateTime.now())
                .build());

        return "Withdrawal successful. New balance: " + account.getBalance();
    }

    public List<BankTransaction> getTransactions(int accountId) {
        return transactionRepo.findByAccountId(accountId);
    }
}

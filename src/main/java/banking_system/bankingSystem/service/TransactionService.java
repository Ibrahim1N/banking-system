package banking_system.bankingSystem.service;


import banking_system.bankingSystem.customException.AccountNotFoundException;
import banking_system.bankingSystem.customException.NotSufficientBalanceException;
import banking_system.bankingSystem.customException.UserNotFoundException;
import banking_system.bankingSystem.model.Account;
import banking_system.bankingSystem.model.Transaction;
import banking_system.bankingSystem.repository.AccountRepository;
import banking_system.bankingSystem.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class TransactionService {
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public TransactionService(AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    public void deposit(String accountNumber, BigDecimal amount) throws AccountNotFoundException {
        Account account = accountRepository.findByAccountNumber(accountNumber);
        if(account == null){
            throw new AccountNotFoundException("Account not found");
        }
        account.setBalance(account.getBalance().add(amount));
        accountRepository.save(account);

        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setTransactionType("DEPOSIT");
        transaction.setFromAccount(null);
        transaction.setToAccount(account);
        transaction.setTimestamp(LocalDateTime.now());
        transactionRepository.save(transaction);
    }

    public void withdraw(String accountNumber, BigDecimal amount) throws AccountNotFoundException, NotSufficientBalanceException {
        Account account = accountRepository.findByAccountNumber(accountNumber);
        if(account == null){
            throw new AccountNotFoundException("Account not found");
        }

        if(account.getBalance().compareTo(amount) < 0){
            throw new NotSufficientBalanceException("Not enough balance");
        }
        account.setBalance(account.getBalance().subtract(amount));
        accountRepository.save(account);

        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setTransactionType("WITHDRAW");
        transaction.setFromAccount(account);
        transaction.setToAccount(null);
        transaction.setTimestamp(LocalDateTime.now());
        transactionRepository.save(transaction);
    }

    @Transactional
    public void transfer(String fromAccountNumber, String toAccountNumber, BigDecimal amount) throws AccountNotFoundException, UserNotFoundException, NotSufficientBalanceException {
        Account fromAccount = accountRepository.findByAccountNumber(fromAccountNumber);
        Account toAccount = accountRepository.findByAccountNumber(toAccountNumber);
        if(fromAccount == null || toAccount == null){
            throw new AccountNotFoundException("Account not found");
        }

        if(fromAccount.getBalance().compareTo(amount) < 0){
            throw new NotSufficientBalanceException("Not enough balance");
        }

        fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
        toAccount.setBalance(toAccount.getBalance().add(amount));
        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setTransactionType("TRANSFER");
        transaction.setFromAccount(fromAccount);
        transaction.setToAccount(toAccount);
        transaction.setTimestamp(LocalDateTime.now());
        transactionRepository.save(transaction);
    }

    public List<Transaction> getTransactionsByAccount(String accountNumber) throws AccountNotFoundException {
        Account account = accountRepository.findByAccountNumber(accountNumber);
        if(account == null){
            throw new AccountNotFoundException("Account does not exist");
        }

        List<Transaction> allTransactions = new ArrayList<>();
        allTransactions.addAll(account.getIncomingTransactions());
        allTransactions.addAll(account.getOutgoingTransactions());
        return allTransactions;

    }
}

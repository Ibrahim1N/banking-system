package banking_system.bankingSystem.service;
import banking_system.bankingSystem.customException.AccountNotFoundException;
import banking_system.bankingSystem.customException.UserNotFoundException;
import banking_system.bankingSystem.model.Account;
import banking_system.bankingSystem.model.User;
import banking_system.bankingSystem.repository.AccountRepository;
import banking_system.bankingSystem.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    public AccountService(AccountRepository accountRepository,  UserRepository userRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

    public Account createAccount(Long userId) throws UserNotFoundException {
        User user = userRepository.findById(userId).orElseThrow(()->new UserNotFoundException("User not found"));

        Account account = new Account();
        account.setUser(user);
        account.setBalance(BigDecimal.ZERO);
        account.setCurrency("AZN");
        account.setStatus("ACTIVE");
        account.setAccountNumber(generateUniqueAccountNumber());

        return accountRepository.save(account);
    }

    public List<Account> getAccountByUser(User user) throws UserNotFoundException {
        User user1 = userRepository.findById(user.getId()).orElseThrow(()-> new UserNotFoundException("User not found"));
        return user1.getAccounts();
    }

    public Account getAccountByNumber(String accountNumber) throws UserNotFoundException, AccountNotFoundException {
        Account account = accountRepository.findByAccountNumber(accountNumber);
        if(account == null){
            throw new AccountNotFoundException("Account not found");
        }
        return account;
    }

    private String generateUniqueAccountNumber() {
        Random random = new Random();
        String accountNumber;
        do {
            accountNumber = "ACCT" + (100000 + random.nextInt(900000));
        } while (accountRepository.findByAccountNumber(accountNumber) != null);
        return accountNumber;
    }
}

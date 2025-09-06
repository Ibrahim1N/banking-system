package banking_system.bankingSystem.customException;

public class NotSufficientBalanceException extends Throwable {
    public NotSufficientBalanceException(String notEnoughBalance) {
        super(notEnoughBalance);
    }
}

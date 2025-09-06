package banking_system.bankingSystem.customException;

public class InvalidCredentialsException extends Throwable {
    public InvalidCredentialsException(String incorrectPassword) {
        super(incorrectPassword);
    }
}

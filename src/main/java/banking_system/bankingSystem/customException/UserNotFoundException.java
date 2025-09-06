package banking_system.bankingSystem.customException;

public class UserNotFoundException extends Throwable {
    public UserNotFoundException(String userNotFound) {
        super(userNotFound);

    }
}

package banking_system.bankingSystem.dto;

public class JwtResponse {
    private String token;

    public JwtResponse() {}

    public JwtResponse(String token) {
        this.token = token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

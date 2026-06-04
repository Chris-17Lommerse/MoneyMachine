package MoneyMachine.models.enums;
import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    USER, 
    EMPLOYEE;

    @Override
    public String getAuthority() {
        return "ROLE_" + name();
    }
}

package MoneyMachine.policies;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;

@Component
public class JwtPolicy {
    
    private final String[] requiredClaims = {"role", "email", "firstName", "lastName", "loginType"};

    public void enforceJwtPolicy(Claims decodedAuthToken) {
        
        enforceDecodedAuthTokenStructure(decodedAuthToken);
        enforceDecodedAuthTokenRequiredClaims(decodedAuthToken);
    }

    public void enforceDecodedAuthTokenStructure(Claims decodedAuthToken) {

        if (decodedAuthToken.getSubject() == null) {
            throw new JwtException("Token is missing subject.");
        }

        if (decodedAuthToken.getExpiration() == null) {
            throw new JwtException("Token is missing expiration.");
        }
    }

    public void enforceDecodedAuthTokenRequiredClaims(Claims decodedAuthToken) {

        for (String requiredClaim : requiredClaims) {
            if (isClaimNullOrEmpty(decodedAuthToken.get(requiredClaim))) {
                throw new JwtException(String.format("Token is missing %s claim.", requiredClaim));
            }
        }
    }

    private boolean isClaimNullOrEmpty(Object claim) {

        if (claim == null) {
            return true;
        }

        if (claim instanceof String str) {
            return str.isBlank();
        }

        return false;
    }
}

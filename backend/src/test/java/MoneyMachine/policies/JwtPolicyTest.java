package MoneyMachine.policies;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.impl.DefaultClaims;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class JwtPolicyTest {

    @InjectMocks
    private JwtPolicy jwtPolicy;

    private Map<String, Object> claimsMap;
    private Claims claims;

    @BeforeEach
    void setUp() {
        claimsMap = new HashMap<>();
        claimsMap.put(Claims.SUBJECT, "1");
        claimsMap.put(Claims.EXPIRATION, new Date(System.currentTimeMillis() + 100000));
        claimsMap.put("role", "USER");
        claimsMap.put("email", "john@doe.com");
        claimsMap.put("firstName", "John");
        claimsMap.put("lastName", "Doe");
        claimsMap.put("loginType", "ATM");

        claims = new DefaultClaims(claimsMap);
    }

    @Test
    void enforceJwtPolicy_whenValidClaims_doesNotThrow() {

        assertDoesNotThrow(() ->
            jwtPolicy.enforceJwtPolicy(claims)
        );
    }

    @Test
    void enforceJwtPolicy_whenMissingSubject_throwsJwtException() {

        claimsMap.remove(Claims.SUBJECT);
        claims = new DefaultClaims(claimsMap);

        assertThrows(JwtException.class, () ->
            jwtPolicy.enforceJwtPolicy(claims)
        );
    }

    @Test
    void enforceJwtPolicy_whenMissingExpiration_throwsJwtException() {

        claimsMap.remove(Claims.EXPIRATION);
        claims = new DefaultClaims(claimsMap);

        assertThrows(JwtException.class, () ->
            jwtPolicy.enforceJwtPolicy(claims)
        );
    }

    @Test
    void enforceJwtPolicy_whenMissingRoleClaim_throwsJwtException() {

        claimsMap.remove("role");
        claims = new DefaultClaims(claimsMap);

        assertThrows(JwtException.class, () ->
            jwtPolicy.enforceJwtPolicy(claims)
        );
    }

    @Test
    void enforceJwtPolicy_whenMissingEmailClaim_throwsJwtException() {

        claimsMap.remove("email");
        claims = new DefaultClaims(claimsMap);

        assertThrows(JwtException.class, () ->
            jwtPolicy.enforceJwtPolicy(claims)
        );
    }

    @Test
    void enforceJwtPolicy_whenMissingFirstNameClaim_throwsJwtException() {

        claimsMap.remove("firstName");
        claims = new DefaultClaims(claimsMap);

        assertThrows(JwtException.class, () ->
            jwtPolicy.enforceJwtPolicy(claims)
        );
    }

    @Test
    void enforceJwtPolicy_whenMissingLastNameClaim_throwsJwtException() {

        claimsMap.remove("lastName");
        claims = new DefaultClaims(claimsMap);

        assertThrows(JwtException.class, () ->
            jwtPolicy.enforceJwtPolicy(claims)
        );
    }

    @Test
    void enforceJwtPolicy_whenMissingLoginTypeClaim_throwsJwtException() {

        claimsMap.remove("loginType");
        claims = new DefaultClaims(claimsMap);

        assertThrows(JwtException.class, () ->
            jwtPolicy.enforceJwtPolicy(claims)
        );
    }

    @Test
    void enforceJwtPolicy_whenBlankRoleClaim_throwsJwtException() {

        claimsMap.put("role", "");
        claims = new DefaultClaims(claimsMap);

        assertThrows(JwtException.class, () ->
            jwtPolicy.enforceJwtPolicy(claims)
        );
    }

    @Test
    void enforceJwtPolicy_whenBlankEmailClaim_throwsJwtException() {

        claimsMap.put("email", "");
        claims = new DefaultClaims(claimsMap);

        assertThrows(JwtException.class, () ->
            jwtPolicy.enforceJwtPolicy(claims)
        );
    }

    @Test
    void enforceJwtPolicy_whenBlankFirstNameClaim_throwsJwtException() {

        claimsMap.put("firstName", "");
        claims = new DefaultClaims(claimsMap);

        assertThrows(JwtException.class, () ->
            jwtPolicy.enforceJwtPolicy(claims)
        );
    }

    @Test
    void enforceJwtPolicy_whenBlankLastNameClaim_throwsJwtException() {

        claimsMap.put("lastName", "");
        claims = new DefaultClaims(claimsMap);

        assertThrows(JwtException.class, () ->
            jwtPolicy.enforceJwtPolicy(claims)
        );
    }

    @Test
    void enforceJwtPolicy_whenBlankLoginTypeClaim_throwsJwtException() {

        claimsMap.put("loginType", "");
        claims = new DefaultClaims(claimsMap);

        assertThrows(JwtException.class, () ->
            jwtPolicy.enforceJwtPolicy(claims)
        );
    }

    @Test
    void enforceJwtPolicy_whenWhitespaceOnlyClaim_throwsJwtException() {

        claimsMap.put("email", "   ");
        claims = new DefaultClaims(claimsMap);

        assertThrows(JwtException.class, () ->
            jwtPolicy.enforceJwtPolicy(claims)
        );
    }

    @Test
    void enforceDecodedAuthTokenStructure_whenValidClaims_doesNotThrow() {

        assertDoesNotThrow(() ->
            jwtPolicy.enforceDecodedAuthTokenStructure(claims)
        );
    }

    @Test
    void enforceDecodedAuthTokenStructure_whenMissingSubject_throwsJwtException() {

        claimsMap.remove(Claims.SUBJECT);
        claims = new DefaultClaims(claimsMap);

        assertThrows(JwtException.class, () ->
            jwtPolicy.enforceDecodedAuthTokenStructure(claims)
        );
    }

    @Test
    void enforceDecodedAuthTokenStructure_whenMissingExpiration_throwsJwtException() {

        claimsMap.remove(Claims.EXPIRATION);
        claims = new DefaultClaims(claimsMap);

        assertThrows(JwtException.class, () ->
            jwtPolicy.enforceDecodedAuthTokenStructure(claims)
        );
    }

    @Test
    void enforceDecodedAuthTokenRequiredClaims_whenValidClaims_doesNotThrow() {

        assertDoesNotThrow(() ->
            jwtPolicy.enforceDecodedAuthTokenRequiredClaims(claims)
        );
    }

    @Test
    void enforceDecodedAuthTokenRequiredClaims_whenAllClaimsMissing_throwsJwtException() {

        claimsMap.remove("role");
        claimsMap.remove("email");
        claimsMap.remove("firstName");
        claimsMap.remove("lastName");
        claimsMap.remove("loginType");
        claims = new DefaultClaims(claimsMap);

        assertThrows(JwtException.class, () ->
            jwtPolicy.enforceDecodedAuthTokenRequiredClaims(claims)
        );
    }
}
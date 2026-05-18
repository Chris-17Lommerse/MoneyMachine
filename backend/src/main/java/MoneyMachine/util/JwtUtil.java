package MoneyMachine.util;

import org.springframework.stereotype.Component;

import MoneyMachine.models.User;
import MoneyMachine.models.enums.LoginType;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;

@Component
public class JwtUtil {
    // Convert the plain text SECRET into a SecretKey object that crypto APIs can use.
    // JWT signing with HS256 uses HMAC (a hash-based message authentication algorithm),
    // which requires raw bytes, not a Java String.
    // We use UTF-8 to convert characters to bytes in a consistent, cross-platform way.
    // This same key must be used for BOTH:
    // - signWith(...) when creating the token
    // - verifyWith(...) when validating/parsing the token
    private final SecretKey authTokenSecretKeyEncoded;
    private final int AUTH_TOKEN_EXPIRATION_HOURS = 1;
    private final String[] requiredClaims = {"role", "email", "firstName", "lastName", "loginType"};

    public JwtUtil(@Value("${AUTH_TOKEN_SECRET_KEY}") String authTokenSecretKey) {
        this.authTokenSecretKeyEncoded = Keys.hmacShaKeyFor(authTokenSecretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String generateAuthTokenFromUser(User user, LoginType loginType) {
        return Jwts.builder()
            .subject(user.getId().toString())
            .claim("role", user.getRole().toString())
            .claim("email", user.getEmail())
            .claim("firstName", user.getFirstName())
            .claim("lastName", user.getLastName())
            .claim("loginType", loginType.toString())
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * AUTH_TOKEN_EXPIRATION_HOURS))
            .signWith(authTokenSecretKeyEncoded)
            .compact();
    }

    public void validateDecodedAuthToken(Claims decodedAuthToken) {

        if (decodedAuthToken.getSubject() == null) {
            throw new JwtException("Token is missing subject.");
        }

        if (decodedAuthToken.getExpiration() == null) {
            throw new JwtException("Token is missing expiration.");
        }

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

    public Claims getDecodedAuthToken(String authToken) {
        return Jwts.parser()
            .verifyWith(authTokenSecretKeyEncoded)
            .build()
            .parseSignedClaims(authToken)
            .getPayload();
    }
}

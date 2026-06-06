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
    private final SecretKey authTokenSecretKeyEncoded;
    private final int AUTH_TOKEN_EXPIRATION_HOURS = 1;
    private final int HOUR_IN_MILLISECONDS = 3600000;

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
            .expiration(getAuthTokenExpirationTime())
            .signWith(authTokenSecretKeyEncoded)
            .compact();
    }

    public Claims getDecodedAuthToken(String authToken) {
        return Jwts.parser()
            .verifyWith(authTokenSecretKeyEncoded)
            .build()
            .parseSignedClaims(authToken)
            .getPayload();
    }

    public Date getAuthTokenExpirationTime() {
        return new Date(System.currentTimeMillis() + HOUR_IN_MILLISECONDS * AUTH_TOKEN_EXPIRATION_HOURS);
    }
}

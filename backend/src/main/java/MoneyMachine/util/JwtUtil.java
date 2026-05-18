package MoneyMachine.util;

import org.springframework.stereotype.Component;

import MoneyMachine.exception.InvalidAuthTokenException;
import MoneyMachine.exception.NotAuthorizedException;
import MoneyMachine.models.enums.LoginType;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

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
    private final String[] requiredClaims = {"role", "email", "firstName", "lastName", "loginType"};

    public JwtUtil(@Value("${AUTH_TOKEN_SECRET_KEY}") String authTokenSecretKey) {
        this.authTokenSecretKeyEncoded = Keys.hmacShaKeyFor(authTokenSecretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(String username) {
        return Jwts.builder()
            .subject(username)
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
            .signWith(authTokenSecretKeyEncoded)
            .compact();
    }

    // @Override
    // public void validateDecodedAuthToken(DecodedJWT decodedAuthToken, LoginType loginType) {

    //     if (decodedAuthToken.getSubject() == null) {
    //         throw new InvalidAuthTokenException("Token is missing subject.");
    //     }

    //     if (decodedAuthToken.getExpiresAt() == null) {
    //         throw new InvalidAuthTokenException("Token is missing expiration.");
    //     }

    //     if (decodedAuthToken.getExpiresAt().before(new Date())) {
    //         throw new InvalidAuthTokenException("Token has expired.");
    //     }

    //     for (String requiredClaim : requiredClaims) {
    //         if (isClaimNullOrEmpty(decodedAuthToken.getClaim(requiredClaim))) {
    //             throw new InvalidAuthTokenException(String.format("Token is missing %s claim.", requiredClaim));
    //         }
    //     }

    //     if (LoginType.valueOf(decodedAuthToken.getClaim("loginType").toString().replace("\"", "")) != loginType){
    //         throw new NotAuthorizedException("You are not logged in into this part of the application.");
    //     }
    // }

    // private boolean isClaimNullOrEmpty(Claim claim) {
    //     return claim == null || claim.isNull() || claim.isMissing() || claim.asString() == null || claim.asString().isBlank();
    // }

    public String extractUsername(String token) {
        
        String jwt = token != null && token.startsWith("Bearer ") ? token.substring(7) : token;
        
        return Jwts.parser()
            .verifyWith(authTokenSecretKeyEncoded)
            .build()
            .parseSignedClaims(jwt)
            .getPayload()
            .getSubject();
    }

    // public User getLoggedInUserByLoginType(HttpServletRequest request, HttpServletResponse response, LoginType loginType) throws Exception {
        
    //     String authHeader = request.getHeader("Authorization");

    //     if (authHeader == null) {
    //         throw new NotAuthorizedException("Authorization header is required.");
    //     }

    //     String[] headerParts = authHeader.split(" ");

    //     if (headerParts.length != 2 || !headerParts[0].equalsIgnoreCase("bearer")) {
    //         throw new NotAuthorizedException("Invalid authorization header format.");
    //     }

    //     String authToken = headerParts[1];
    //     DecodedJWT decodedAuthToken = getDecodedAuthToken(authToken);
    //     validateDecodedAuthToken(decodedAuthToken, loginType);
        
    //     User user = this.userRepository.findById(Integer.parseInt(decodedAuthToken.getSubject()));
        
    //     if (user == null) {
    //         throw new InvalidAuthTokenException("User in your token does not exist.");
    //     }

    //     return user;
    // }
}

package MoneyMachine.filter;

import MoneyMachine.exception.NotAuthorizedException;
import MoneyMachine.models.User;
import MoneyMachine.repositories.UserRepository;
import MoneyMachine.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.util.Optional;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final HandlerExceptionResolver handlerExceptionResolver;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, UserRepository userRepository, HandlerExceptionResolver handlerExceptionResolver) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.handlerExceptionResolver = handlerExceptionResolver;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
        
        try {
            String authorizationHeader = request.getHeader("Authorization");

            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }

            String[] headerParts = authorizationHeader.split(" ");

            if (headerParts.length != 2 || !headerParts[0].equalsIgnoreCase("bearer")) {
                throw new NotAuthorizedException("Invalid authorization header format.");
            }

            String authToken = headerParts[1];
            
            Claims decodedAuthToken = jwtUtil.getDecodedAuthToken(authToken);
            jwtUtil.validateDecodedAuthToken(decodedAuthToken);
            Optional<User> userOptional = userRepository.findById(Long.parseLong(decodedAuthToken.getSubject()));
            
            if (!userOptional.isPresent()) {
                throw new JwtException("User in auth token does not exist.");
            }

            User user = userOptional.get();
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            
            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            filterChain.doFilter(request, response);
        } 
        catch (Exception e) {
            handlerExceptionResolver.resolveException(request, response, null, e);
        }
    }
}

package MoneyMachine.filter;

import MoneyMachine.exception.NotAuthorizedException;
import MoneyMachine.exception.NotFoundException;
import MoneyMachine.models.User;
import MoneyMachine.policies.JwtPolicy;
import MoneyMachine.services.interfaces.UserService;
import MoneyMachine.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final JwtPolicy jwtPolicy;
    private final UserService userService;
    private final HandlerExceptionResolver handlerExceptionResolver;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, JwtPolicy jwtPolicy, UserService userService, HandlerExceptionResolver handlerExceptionResolver) {
        this.jwtUtil = jwtUtil;
        this.jwtPolicy = jwtPolicy;
        this.userService = userService;
        this.handlerExceptionResolver = handlerExceptionResolver;
    }

    private String getAuthTokenFromAuthHeader(String authorizationHeader) {

        String[] headerParts = authorizationHeader.split(" ");

        if (headerParts.length != 2 || !headerParts[0].equalsIgnoreCase("bearer")) {
            throw new NotAuthorizedException("Invalid authorization header format.");
        }

        return headerParts[1];
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {

        try {
            // CAN BE REMOVED IF HOSTING IS CORRECTLY CONFIGURED
            if("OPTIONS".equalsIgnoreCase(request.getMethod())) {
                filterChain.doFilter(request, response);
                return;
            }
            
            String authorizationHeader = request.getHeader("Authorization");

            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }

            String authToken = getAuthTokenFromAuthHeader(authorizationHeader);
            Claims decodedAuthToken = jwtUtil.getDecodedAuthToken(authToken);
            jwtPolicy.enforceJwtPolicy(decodedAuthToken);

            User user = null;

            try {
                user = userService.getUserById(Long.parseLong(decodedAuthToken.getSubject()));
            }
            catch (NotFoundException e) {
                throw new JwtException(String.format("User in auth token does not exist: %s", e.getMessage()));
            }

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            authentication.setDetails(decodedAuthToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            filterChain.doFilter(request, response);
        } 
        catch (Exception e) {
            handlerExceptionResolver.resolveException(request, response, null, e);
        }
    }
}

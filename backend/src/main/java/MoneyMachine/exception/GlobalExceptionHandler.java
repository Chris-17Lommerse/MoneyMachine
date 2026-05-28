package MoneyMachine.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import MoneyMachine.models.enums.ErrorType;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import MoneyMachine.models.dtos.responses.ErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private String getLocationOfException(Exception ex) {

        StackTraceElement[] trace = ex.getStackTrace();
        String locationInfo = "unknown location";

        if (trace.length > 0) {
            StackTraceElement targetElement = ex.getStackTrace()[0];

            locationInfo = String.format("%s.%s(%s:%d)",
                    targetElement.getClassName(),
                    targetElement.getMethodName(),
                    targetElement.getFileName(),
                    targetElement.getLineNumber());
        }
        
        return locationInfo;
    }

   @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAllExceptions(Exception ex) {
        
        String locationInfo = getLocationOfException(ex);
        ErrorResponse errorResponse = new ErrorResponse(500, ErrorType.INTERNAL_SERVER_ERROR, ex.getMessage(), locationInfo);

        System.err.println(String.format("[RUNTIME ERROR] Reason: %s | Code Location: %s", ex.getMessage(), locationInfo));
        return ResponseEntity.status(errorResponse.getCode()).body(errorResponse); 
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleInvalidCredentialsException(InvalidCredentialsException ex) {
        
        ErrorResponse errorResponse = new ErrorResponse(401, ErrorType.UNAUTHORIZED, "Invalid credentials", ex.getMessage());
        return ResponseEntity.status(errorResponse.getCode()).body(errorResponse); 
    }

    @ExceptionHandler(NotAuthorizedException.class)
    public ResponseEntity<ErrorResponse> handleNotAuthorizedExceptions(NotAuthorizedException ex) {
        
        ErrorResponse errorResponse = new ErrorResponse(401, ErrorType.UNAUTHORIZED, "Not authorized", ex.getMessage());
        return ResponseEntity.status(errorResponse.getCode()).body(errorResponse); 
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedExceptions(AccessDeniedException ex) {
        
        ErrorResponse errorResponse = new ErrorResponse(403, ErrorType.FORBIDDEN, "Access denied", ex.getMessage());
        return ResponseEntity.status(errorResponse.getCode()).body(errorResponse); 
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ErrorResponse> handleJwtExceptions(JwtException ex) {
        
        ErrorResponse errorResponse = new ErrorResponse(401, ErrorType.INVALID_AUTH_TOKEN, "Invalid token", ex.getMessage());
        return ResponseEntity.status(errorResponse.getCode()).body(errorResponse); 
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ErrorResponse> handleExpiredJwtExceptions(ExpiredJwtException ex) {
        
        ErrorResponse errorResponse = new ErrorResponse(401, ErrorType.INVALID_AUTH_TOKEN, "Expired token", "Your authentication token has expired.");
        return ResponseEntity.status(errorResponse.getCode()).body(errorResponse); 
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentExceptions(IllegalArgumentException ex) {
        
        ErrorResponse errorResponse = new ErrorResponse(400, ErrorType.ILLEGAL_ARGUMENTS, "Illegal arguments", ex.getMessage());
        return ResponseEntity.status(errorResponse.getCode()).body(errorResponse); 
    }
}
package MoneyMachine.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.auth0.jwt.exceptions.JWTDecodeException;

import MoneyMachine.models.dtos.ErrorResponse;
import MoneyMachine.models.enums.ErrorType;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private String getLocationOfException(Exception ex){

        StackTraceElement[] trace = ex.getStackTrace();
        String locationInfo = "unknown location";

        if (trace.length > 0) {
            StackTraceElement targetElement = ex.getStackTrace()[0];

            locationInfo = String.format("%s.%s(%s:%d)", 
                targetElement.getClassName(), 
                targetElement.getMethodName(), 
                targetElement.getFileName(), 
                targetElement.getLineNumber()
            );
        }

        System.err.println(String.format("[RUNTIME ERROR] Reason: %s | Code Location: %s", ex.getMessage(), locationInfo));

        return locationInfo;
    }

    private ErrorResponse generateErrorDtoByExceptionAndErrorInfo(Exception ex, int code, ErrorType errorType, String message) {
        
        if (message == null){
            String locationInfo = getLocationOfException(ex);
            return new ErrorResponse(code, errorType, ex.getMessage(), locationInfo);
        }

        return new ErrorResponse(code, errorType, message, ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAllExceptions(Exception ex) {
        
        ErrorResponse errorDTO = generateErrorDtoByExceptionAndErrorInfo(ex, 500, ErrorType.UNAUTHORIZED, null);
        return ResponseEntity.status(errorDTO.getCode()).body(errorDTO); 
    }

    @ExceptionHandler(JWTDecodeException.class)
    public ResponseEntity<ErrorResponse> handleJwtDecodeException(JWTDecodeException ex) {
        
        ErrorResponse errorDTO = generateErrorDtoByExceptionAndErrorInfo(ex, 401, ErrorType.INVALID_AUTH_TOKEN, "Failed to decode JWT");
        return ResponseEntity.status(errorDTO.getCode()).body(errorDTO); 
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleInvalidCredentialsException(InvalidCredentialsException ex) {
        
        ErrorResponse errorDTO = generateErrorDtoByExceptionAndErrorInfo(ex, 401, ErrorType.UNAUTHORIZED, "Invalid credentials");
        return ResponseEntity.status(errorDTO.getCode()).body(errorDTO); 
    }

    @ExceptionHandler(NotAuthorizedException.class)
    public ResponseEntity<ErrorResponse> handleNotAuthorizedExceptions(NotAuthorizedException ex) {
        
        ErrorResponse errorDTO = generateErrorDtoByExceptionAndErrorInfo(ex, 401, ErrorType.UNAUTHORIZED, "Not authorized");
        return ResponseEntity.status(errorDTO.getCode()).body(errorDTO); 
    }

    @ExceptionHandler(InvalidAuthTokenException.class)
    public ResponseEntity<ErrorResponse> handleInvalidAuthTokenExceptions(InvalidAuthTokenException ex) {
        
        ErrorResponse errorDTO = generateErrorDtoByExceptionAndErrorInfo(ex, 401, ErrorType.INVALID_AUTH_TOKEN, "Invalid token");
        return ResponseEntity.status(errorDTO.getCode()).body(errorDTO); 
    }
}
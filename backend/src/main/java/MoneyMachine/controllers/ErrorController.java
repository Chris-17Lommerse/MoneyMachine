package MoneyMachine.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.auth0.jwt.exceptions.JWTDecodeException;

import MoneyMachine.models.dtos.ErrorDTO;
import MoneyMachine.models.enums.ErrorType;
import MoneyMachine.models.exceptions.ExpiredException;
import MoneyMachine.models.exceptions.NotAuthorizedException;

@RestControllerAdvice
public class ErrorController {

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

    private ErrorDTO generateErrorDtoByExceptionAndErrorInfo(Exception ex, int code, ErrorType errorType, String message) {
        
        if (message == null){
            String locationInfo = getLocationOfException(ex);
            return new ErrorDTO(code, errorType, ex.getMessage(), locationInfo);
        }

        return new ErrorDTO(code, errorType, message, ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDTO> handleAllExceptions(Exception ex) {
        
        ErrorDTO errorDTO = generateErrorDtoByExceptionAndErrorInfo(ex, 500, ErrorType.UNAUTHORIZED, null);
        return ResponseEntity.status(errorDTO.getCode()).body(errorDTO); 
    }

    @ExceptionHandler(NotAuthorizedException.class)
    public ResponseEntity<ErrorDTO> handleNotAuthorizedExceptions(NotAuthorizedException ex) {
        
        ErrorDTO errorDTO = generateErrorDtoByExceptionAndErrorInfo(ex, 401, ErrorType.UNAUTHORIZED, "Not authorized");
        return ResponseEntity.status(errorDTO.getCode()).body(errorDTO); 
    }

    @ExceptionHandler(ExpiredException.class)
    public ResponseEntity<ErrorDTO> handleExpiredExceptionExceptions(ExpiredException ex) {
        
        ErrorDTO errorDTO = generateErrorDtoByExceptionAndErrorInfo(ex, 401, ErrorType.UNAUTHORIZED, "Auth token expired");
        return ResponseEntity.status(errorDTO.getCode()).body(errorDTO); 
    }

    @ExceptionHandler(JWTDecodeException.class)
    public ResponseEntity<ErrorDTO> handleJwtDecodeException(JWTDecodeException ex) {
        
        ErrorDTO errorDTO = generateErrorDtoByExceptionAndErrorInfo(ex, 401, ErrorType.UNAUTHORIZED, "Jwt failed to decode");
        return ResponseEntity.status(errorDTO.getCode()).body(errorDTO); 
    }
}
package MoneyMachine.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import MoneyMachine.models.dtos.ErrorDTO;
import MoneyMachine.models.enums.ErrorType;

@RestControllerAdvice
public class ErrorController {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDTO> handleAllExceptions(Exception ex) {
        
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
        ErrorDTO errorDTO = new ErrorDTO(500, ErrorType.INTERNAL_SERVER_ERROR, ex.getMessage(), locationInfo);

        return ResponseEntity.status(errorDTO.getCode()).body(errorDTO); 
    }
}
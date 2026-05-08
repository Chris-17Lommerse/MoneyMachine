package MoneyMachine;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String APP_PACKAGE_PREFIX = "MoneyMachine";

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public void handleAllExceptions(Exception ex) {
        
        Throwable cause = ex;
        while (cause.getCause() != null) {
            cause = cause.getCause();
        }

        StackTraceElement[] trace = cause.getStackTrace();
        String locationInfo = "Unknown Source";

        System.out.println(trace.length);


        for (StackTraceElement el : ex.getStackTrace()){
            System.err.println(el.getClassName());
        }

        if (trace != null && trace.length > 0) {
            // Searches specifically for the line inside your MoneyMachine package files
            StackTraceElement targetElement = Arrays.stream(trace)
                .filter(e -> e.getClassName().startsWith(APP_PACKAGE_PREFIX))
                .findFirst()
                .orElse(trace[0]); // Falls back to root cause only if your package isn't in the trace

            locationInfo = String.format("%s.%s(%s:%d)", 
                targetElement.getClassName(), 
                targetElement.getMethodName(), 
                targetElement.getFileName(), 
                targetElement.getLineNumber()
            );

            System.out.println(targetElement.getClassName());
        }

        System.err.println(String.format("[CRITICAL FAILURE] Reason: %s | Code Location: %s", ex.getMessage(), locationInfo));
    }
}

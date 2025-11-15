
package backend.grupo130.camiones.config.exceptions;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ServiceError.class)
    public ResponseEntity<Map<String, Object>> handleServiceError(ServiceError ex) {

        HttpStatus status = HttpStatus.valueOf(ex.getHttpCode());

        Map<String, Object> errorDetails = new HashMap<>();

        errorDetails.put("httpCode", ex.getHttpCode());
        errorDetails.put("message", ex.getMessage());
        errorDetails.put("mensajeInterno", ex.getMensajeExterno());

        return ResponseEntity.status(status).body(errorDetails);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {

        Map<String, Object> errorDetails = new HashMap<>();

        errorDetails.put("status", 400);

        ex.getBindingResult().getAllErrors().forEach((error) -> {

            String fieldName = ((org.springframework.validation.FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();

            errorDetails.put(fieldName, errorMessage);
        });

        return ResponseEntity.badRequest().body(errorDetails);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, Object>> handleConstraintViolationException(ConstraintViolationException ex) {

        Map<String, Object> errorDetails = new HashMap<>();

        errorDetails.put("status", 400);

        ex.getConstraintViolations().forEach(violation -> {

            String propertyPath = violation.getPropertyPath().toString();
            String message = violation.getMessage();

            errorDetails.put(propertyPath, message);
        });

        return ResponseEntity.badRequest().body(errorDetails);
    }

}

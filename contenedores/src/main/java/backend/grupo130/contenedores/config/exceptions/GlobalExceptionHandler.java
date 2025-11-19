package backend.grupo130.contenedores.config.exceptions;

import backend.grupo130.contenedores.config.enums.Errores;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(ServiceError.class)
    public ResponseEntity<Map<String, Object>> handleServiceError(ServiceError ex) {

        log.warn("Error Controlado: {} - Detalle: {}", ex.getMensajeExterno(), ex.getMessage());

        HttpStatus status = HttpStatus.valueOf(ex.getHttpCode());

        Map<String, Object> errorDetails = new HashMap<>();

        errorDetails.put("httpCode", ex.getHttpCode());
        errorDetails.put("mensajeInterno", ex.getMessage());
        errorDetails.put("mensajeExterno", ex.getMensajeExterno());

        return ResponseEntity.status(status).body(errorDetails);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        log.error("ERROR INTERNO NO CONTROLADO: ", ex);

        Map<String, Object> errorDetails = new HashMap<>();

        errorDetails.put("httpCode", 500);
        errorDetails.put("mensajeInterno", ex.getMessage());
        errorDetails.put("mensajeExterno", Errores.ERROR_INTERNO.getMensaje());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDetails);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {

        log.warn("Error de Validación de Argumentos: {}", ex.getMessage());

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

        log.warn("Error de Validación de Constraints: {}", ex.getMessage()); // Opcional

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

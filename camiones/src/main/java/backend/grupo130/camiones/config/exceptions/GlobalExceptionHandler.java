
package backend.grupo130.camiones.config.exceptions;

import backend.grupo130.camiones.config.enums.Errores;
import backend.grupo130.camiones.dto.ErrorResponse;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(ServiceError.class)
    public ResponseEntity<ErrorResponse> handleServiceError(ServiceError ex) {

        log.warn("Error Controlado: {} - Detalle: {}", ex.getMensajeExterno(), ex.getMessage());

        HttpStatus status = HttpStatus.valueOf(ex.getHttpCode());

        ErrorResponse errorDetails = ErrorResponse.builder()
            .httpCode(ex.getHttpCode())
            .mensajeInterno(ex.getMessage())
            .mensajeExterno(ex.getMensajeExterno())
            .timestamp(LocalDateTime.now())
            .build();

        return ResponseEntity.status(status).body(errorDetails);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        log.error("ERROR INTERNO NO CONTROLADO: ", ex);

        ErrorResponse errorDetails = ErrorResponse.builder()
            .httpCode(500)
            .mensajeInterno(ex.getMessage())
            .mensajeExterno(Errores.ERROR_INTERNO.getMensaje())
            .timestamp(LocalDateTime.now())
            .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDetails);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {

        log.warn("Error de Validación de Argumentos: {}", ex.getMessage());

        Map<String, Object> errorDetails = new HashMap<>();

        errorDetails.put("status", 400);
        errorDetails.put("timestamp", LocalDateTime.now());

        ex.getBindingResult().getAllErrors().forEach((error) -> {

            String fieldName = ((org.springframework.validation.FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();

            errorDetails.put(fieldName, errorMessage);
        });

        return ResponseEntity.badRequest().body(errorDetails);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, Object>> handleConstraintViolationException(ConstraintViolationException ex) {

        log.warn("Error de Validación de Constraints: {}", ex.getMessage());

        Map<String, Object> errorDetails = new HashMap<>();

        errorDetails.put("status", 400);
        errorDetails.put("timestamp", LocalDateTime.now());

        ex.getConstraintViolations().forEach(violation -> {

            String propertyPath = violation.getPropertyPath().toString();
            String message = violation.getMessage();

            errorDetails.put(propertyPath, message);
        });

        return ResponseEntity.badRequest().body(errorDetails);
    }

}

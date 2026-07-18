package backend.grupo130.tramos.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    private int httpCode;
    private String mensajeInterno;
    private String mensajeExterno;
    private LocalDateTime timestamp;
}

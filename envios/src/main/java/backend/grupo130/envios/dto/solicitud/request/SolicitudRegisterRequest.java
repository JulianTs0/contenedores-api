package backend.grupo130.envios.dto.solicitud.request;

import backend.grupo130.envios.data.models.SeguimientoEnvio;
import backend.grupo130.envios.data.models.Tarifa;
import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class SolicitudRegisterRequest {

    // --- DATOS DEL CONTENEDOR (NUEVOS) ---
    @NotNull(message = "El peso es obligatorio")
    @Positive(message = "El peso debe ser positivo")
    @Digits(integer = 8, fraction = 2, message = "Formato de peso inválido")
    private final BigDecimal peso;

    @NotNull(message = "El volumen es obligatorio")
    @Positive(message = "El volumen debe ser positivo")
    @Digits(integer = 8, fraction = 2, message = "Formato de volumen inválido")
    private final BigDecimal volumen;
    // --- FIN DATOS CONTENEDOR ---

    @NotNull(message = "El ID de cliente no puede ser nulo")
    @Positive(message = "El ID de cliente debe ser positivo")
    private final Long idCliente;

}

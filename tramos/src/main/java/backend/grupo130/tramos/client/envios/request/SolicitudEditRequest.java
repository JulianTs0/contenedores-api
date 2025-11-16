package backend.grupo130.tramos.client.envios.request;

import backend.grupo130.tramos.client.envios.models.Tarifa;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SolicitudEditRequest {

    @NotNull(message = "El ID de solicitud no puede ser nulo")
    @Positive(message = "El ID de solicitud debe ser positivo")
    private Long idSolicitud;

    @FutureOrPresent(message = "La fecha de inicio no puede ser en el pasado")
    private LocalDateTime fechaInicio;

    @FutureOrPresent(message = "La fecha de fin no puede ser en el pasado")
    private LocalDateTime fechaFin;

    @Digits(integer = 10, fraction = 2, message = "Formato de costo inválido")
    private BigDecimal costoEstimado;

    @Digits(integer = 10, fraction = 2, message = "Formato de costo inválido")
    private BigDecimal costoFinal;

    @Digits(integer = 10, fraction = 2, message = "Formato de tiempo inválido")
    private BigDecimal tiempoEstimadoHoras;

    @Digits(integer = 10, fraction = 2, message = "Formato de tiempo inválido")
    private BigDecimal tiempoRealHoras;

    private Tarifa tarifa;
}

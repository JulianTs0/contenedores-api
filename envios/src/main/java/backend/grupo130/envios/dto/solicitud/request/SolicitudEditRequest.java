package backend.grupo130.envios.dto.solicitud.request;

import backend.grupo130.envios.data.models.Tarifa;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class SolicitudEditRequest {

    @NotNull(message = "El ID de solicitud no puede ser nulo")
    @Positive(message = "El ID de solicitud debe ser positivo")
    private final Long idSolicitud;

    @FutureOrPresent(message = "La fecha de inicio no puede ser en el pasado")
    private final LocalDateTime fechaInicio;

    @FutureOrPresent(message = "La fecha de fin no puede ser en el pasado")
    private final LocalDateTime fechaFin;

    @Digits(integer = 10, fraction = 2, message = "Formato de costo inválido")
    private final BigDecimal costoEstimado;

    @Digits(integer = 10, fraction = 2, message = "Formato de costo inválido")
    private final BigDecimal costoFinal;

    @Digits(integer = 10, fraction = 2, message = "Formato de tiempo inválido")
    private final BigDecimal tiempoEstimadoHoras;

    @Digits(integer = 10, fraction = 2, message = "Formato de tiempo inválido")
    private final BigDecimal tiempoRealHoras;

    private final Tarifa tarifa;

    private Long idOrigen;

    private Long idDestino;

}

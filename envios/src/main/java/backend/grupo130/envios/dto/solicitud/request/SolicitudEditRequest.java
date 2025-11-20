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

    @NotNull(message = "{error.idSolicitud.notNull}")
    @Positive(message = "{error.idSolicitud.positive}")
    private final Long idSolicitud;

    @FutureOrPresent(message = "{error.fecha.futureOrPresent}")
    private final LocalDateTime fechaInicio;

    @FutureOrPresent(message = "{error.fecha.futureOrPresent}")
    private final LocalDateTime fechaFin;

    @Digits(integer = 10, fraction = 2, message = "{error.costo.digits}")
    private final BigDecimal costoEstimado;

    @Digits(integer = 10, fraction = 2, message = "{error.costo.digits}")
    private final BigDecimal costoFinal;

    @Digits(integer = 10, fraction = 2, message = "{error.tiempo.digits}")
    private final BigDecimal tiempoEstimadoHoras;

    @Digits(integer = 10, fraction = 2, message = "{error.tiempo.digits}")
    private final BigDecimal tiempoRealHoras;

    private final Tarifa tarifa;

    private Long idOrigen;

    private Long idDestino;

}

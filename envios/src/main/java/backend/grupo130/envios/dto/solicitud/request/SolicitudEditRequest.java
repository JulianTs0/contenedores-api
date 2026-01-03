package backend.grupo130.envios.dto.solicitud.request;

import backend.grupo130.envios.data.entity.Tarifa;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class SolicitudEditRequest {

    private final Long idSolicitud;

    @FutureOrPresent(message = "{error.fecha.futureOrPresent}")
    private final LocalDateTime fechaInicio;

    @FutureOrPresent(message = "{error.fecha.futureOrPresent}")
    private final LocalDateTime fechaFin;

    @Digits(integer = 10, fraction = 2, message = "{error.tiempo.digits}")
    @Positive(message = "{error.tiempo.positive}")
    private final BigDecimal tiempoEstimadoHoras;

    @Digits(integer = 10, fraction = 2, message = "{error.tiempo.digits}")
    @Positive(message = "{error.tiempo.positive}")
    private final BigDecimal tiempoRealHoras;

    @Valid
    private final Tarifa tarifa;

    @Positive(message = "{error.idOrigen.positive}")
    private Long idOrigen;

    @Positive(message = "{error.idDestino.positive}")
    private Long idDestino;

}

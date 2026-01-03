package backend.grupo130.tramos.client.envios.request;

import backend.grupo130.tramos.client.envios.entity.Tarifa;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class SolicitudEditRequest {

    private final Long idSolicitud;

    private final LocalDateTime fechaInicio;

    private final LocalDateTime fechaFin;

    private final BigDecimal tiempoEstimadoHoras;

    private final BigDecimal tiempoRealHoras;

    private final Tarifa tarifa;

    private final Long idOrigen;

    private final Long idDestino;

}

package backend.grupo130.tramos.client.envios.responses;

import backend.grupo130.tramos.client.envios.models.Tarifa;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class SolicitudEditResponse {

    private final Long idSolicitud;

    private final String estado;

    private final LocalDateTime fechaInicio;

    private final LocalDateTime fechaFin;

    private final BigDecimal costoEstimado;

    private final BigDecimal costoFinal;

    private final BigDecimal tiempoEstimadoHoras;

    private final BigDecimal tiempoRealHoras;

    private final Tarifa tarifa;

    private Long idOrigen;

    private Long idDestino;
}

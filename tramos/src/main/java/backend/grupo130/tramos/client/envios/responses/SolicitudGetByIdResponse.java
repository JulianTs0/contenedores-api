package backend.grupo130.tramos.client.envios.responses;

import backend.grupo130.tramos.client.envios.entity.SeguimientoEnvio;
import backend.grupo130.tramos.client.envios.entity.Tarifa;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


@Getter
@AllArgsConstructor
public class SolicitudGetByIdResponse {

    private final Long idSolicitud;

    private final LocalDateTime fechaInicio;

    private final LocalDateTime fechaFin;

    private final String estado;

    private final Tarifa tarifa;

    private final List<SeguimientoEnvio> seguimientos;

    private final Long idContenedor;

    private final Long idCliente;

    private final Long idOrigen;

    private final Long idDestino;

    private final BigDecimal costoEstimado;

    private final BigDecimal costoFinal;

    private final BigDecimal tiempoEstimadoHoras;

    private final BigDecimal tiempoRealHoras;

}

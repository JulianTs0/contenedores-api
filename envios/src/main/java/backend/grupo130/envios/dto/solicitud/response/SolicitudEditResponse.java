package backend.grupo130.envios.dto.solicitud.response;

import backend.grupo130.envios.data.entity.SeguimientoEnvio;
import backend.grupo130.envios.data.entity.Tarifa;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class SolicitudEditResponse {

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

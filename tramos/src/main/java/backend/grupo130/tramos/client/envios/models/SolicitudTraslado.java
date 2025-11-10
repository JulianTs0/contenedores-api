package backend.grupo130.tramos.client.envios.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SolicitudTraslado {

    private Integer idSolicitud;

    private LocalDateTime fechaInicio;

    private BigDecimal costoEstimado;

    private BigDecimal tiempoEstimadoHoras;

    private BigDecimal costoFinal;

    private BigDecimal tiempoRealHoras;

    private Integer idContenedor;

    private Integer idCliente;

    private Integer idOrigen;

    private Integer idDestino;

    private Integer idTarifa;

}

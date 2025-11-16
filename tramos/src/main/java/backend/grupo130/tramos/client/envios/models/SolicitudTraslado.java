package backend.grupo130.tramos.client.envios.models;

import backend.grupo130.tramos.config.enums.EstadoSolicitud;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SolicitudTraslado {

    private Long idSolicitud;

    private LocalDateTime fechaInicio;

    private LocalDateTime fechaFin;

    private BigDecimal tiempoEstimadoHoras;

    private BigDecimal tiempoRealHoras;

    private BigDecimal costoEstimado;

    private BigDecimal costoFinal;

    private Tarifa tarifa;

    private List<SeguimientoEnvio> seguimientos;

    private Long idContenedor;

    private Long idCliente;

    private Long idOrigen;

    private Long idDestino;

}

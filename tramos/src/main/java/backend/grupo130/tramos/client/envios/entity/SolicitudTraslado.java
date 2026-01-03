package backend.grupo130.tramos.client.envios.entity;

import backend.grupo130.tramos.config.enums.EstadoSolicitud;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


@Data
@Builder
@AllArgsConstructor
public class SolicitudTraslado {

    private Long idSolicitud;

    private LocalDateTime fechaInicio;

    private LocalDateTime fechaFin;

    private BigDecimal tiempoEstimadoHoras;

    private BigDecimal tiempoRealHoras;

    private EstadoSolicitud estado;

    private Tarifa tarifa;

    private List<SeguimientoEnvio> seguimientos;

    private Long idContenedor;

    private Long idCliente;

    private Long idOrigen;

    private Long idDestino;

    public boolean esBorrador(){
        return this.estado.equals(EstadoSolicitud.BORRADOR);
    }

    public boolean esConfirmada(){
        return this.estado.equals(EstadoSolicitud.CONFIRMADA);
    }

    public boolean esProgramada(){
        return this.estado.equals(EstadoSolicitud.PROGRAMADO);
    }

    public boolean esEntregada(){
        return this.estado.equals(EstadoSolicitud.ENTREGADO);
    }

    public boolean esEntransito(){
        return this.estado.equals(EstadoSolicitud.EN_TRANSITO);
    }

}

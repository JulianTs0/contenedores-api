package backend.grupo130.envios.data.entity;

import backend.grupo130.envios.config.enums.EstadoSolicitud;
import backend.grupo130.envios.data.entity.SeguimientoEnvio;
import backend.grupo130.envios.data.entity.Tarifa;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SolicitudTraslado {

    private Long idSolicitud;

    private LocalDateTime fechaInicio;

    private LocalDateTime fechaFin;

    private BigDecimal costoEstimado;

    private BigDecimal costoFinal;

    private BigDecimal tiempoEstimadoHoras;

    private BigDecimal tiempoRealHoras;

    private Tarifa tarifa;

    private EstadoSolicitud estado;

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

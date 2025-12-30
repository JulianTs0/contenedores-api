package backend.grupo130.envios.data.entity;

import backend.grupo130.envios.config.enums.Descripcciones;
import backend.grupo130.envios.config.enums.Errores;
import backend.grupo130.envios.config.enums.EstadoSolicitud;
import backend.grupo130.envios.config.exceptions.ServiceError;
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

// TODO: Revisar los datos inutiles con respecto a la solicitud

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

    public void transicionarEstado(EstadoSolicitud nuevoEstado, String descripcion){

        switch (this.getEstado()) {

            case BORRADOR:

                if (nuevoEstado != EstadoSolicitud.CONFIRMADA)
                    throw new ServiceError("", Errores.TRANSICION_ESTADO_INVALIDA, 400);

                break;
            case CONFIRMADA:

                if (nuevoEstado != EstadoSolicitud.PROGRAMADO)
                    throw new ServiceError("", Errores.TRANSICION_ESTADO_INVALIDA, 400);

                break;
            case PROGRAMADO:

                if (nuevoEstado != EstadoSolicitud.EN_TRANSITO)
                    throw new ServiceError("", Errores.TRANSICION_ESTADO_INVALIDA, 400);

                break;
            case EN_TRANSITO:

                if (nuevoEstado != EstadoSolicitud.ENTREGADO)
                    throw new ServiceError("", Errores.TRANSICION_ESTADO_INVALIDA, 400);

                break;
            case ENTREGADO:

                throw new ServiceError("", Errores.SOLICITUD_YA_FINALIZADA, 400);
        }

        if(nuevoEstado.equals(EstadoSolicitud.EN_TRANSITO)) {
            this.setFechaInicio(LocalDateTime.now());
        }

        if(nuevoEstado.equals(EstadoSolicitud.ENTREGADO)) {
            this.setFechaFin(LocalDateTime.now());
        }

        this.setEstado(nuevoEstado);

        SeguimientoEnvio nuevoSeguimiento = new SeguimientoEnvio(
            null,
            LocalDateTime.now(),
            (nuevoEstado == EstadoSolicitud.ENTREGADO) ? LocalDateTime.now() : null,
            nuevoEstado,
            descripcion
        );

        this.getSeguimientos().getLast().setFechaHoraFin(LocalDateTime.now());
        this.getSeguimientos().add(nuevoSeguimiento);

    }

}

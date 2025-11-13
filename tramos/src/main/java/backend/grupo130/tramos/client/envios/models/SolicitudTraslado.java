package backend.grupo130.tramos.client.envios.models;

import backend.grupo130.tramos.config.enums.Estado;
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

    private LocalDateTime fechaHoraInicio;

    private LocalDateTime fechaHoraFin;

    private BigDecimal costoEstimado;

    private BigDecimal tiempoEstimadoHoras;

    private BigDecimal costoFinal;

    private BigDecimal tiempoRealHoras;

    private Integer idContenedor;

    private Integer idCliente;

    private Integer idOrigen;

    private Integer idDestino;

    private Integer idTarifa;

    private Estado estado;

    private String descripcion;

    public boolean esBorrador(){
        return this.estado.equals(Estado.BORRADOR);
    }

    public boolean esProgramada(){
        return this.estado.equals(Estado.PROGRAMADA);
    }

    public boolean esEntransito(){
        return this.estado.equals(Estado.ENTRANSITO);
    }

    public boolean esEntregada(){
        return this.estado.equals(Estado.ENTREGADA);
    }

}

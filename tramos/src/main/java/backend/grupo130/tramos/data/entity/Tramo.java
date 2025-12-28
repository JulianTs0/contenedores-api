package backend.grupo130.tramos.data.entity;

import backend.grupo130.tramos.config.enums.EstadoTramo;
import backend.grupo130.tramos.config.enums.TipoTramo;
import backend.grupo130.tramos.data.models.RutaTrasladoModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tramo {

    private Long idTramo;

    private TipoTramo tipoTramo;

    private EstadoTramo estado;

    private BigDecimal costoAproximado;

    private BigDecimal costoReal;

    private LocalDateTime fechaHoraInicioEstimado;

    private LocalDateTime fechaHoraFinEstimado;

    private LocalDateTime fechaHoraInicioReal;

    private LocalDateTime fechaHoraFinReal;

    private Integer orden;

    private String dominioCamion;

    private RutaTraslado rutaTraslado;

    private Long idOrigen;

    private Long idDestino;

    private Double distancia;

    public boolean esEstimado(){
        return estado.equals(EstadoTramo.ESTIMADO);
    }

    public boolean esAsignado() {
        return  estado.equals(EstadoTramo.ASIGNADO);
    }

    public boolean esIniciado() {
        return  estado.equals(EstadoTramo.INICIADO);
    }

    public boolean esFinalizado(){
        return  estado.equals(EstadoTramo.FINALIZADO);
    }

}

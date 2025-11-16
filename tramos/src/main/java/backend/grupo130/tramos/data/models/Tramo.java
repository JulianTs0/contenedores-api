package backend.grupo130.tramos.data.models;

import backend.grupo130.tramos.config.enums.EstadoTramo;
import backend.grupo130.tramos.config.enums.TipoTramo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "tramos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tramo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tramo")
    private Long idTramo;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_tramo", nullable = false)
    private TipoTramo tipoTramo;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoTramo estado;

    @Column(name = "costo_aproximado", precision = 10, scale = 2)
    private BigDecimal costoAproximado;

    @Column(name = "costo_real", precision = 10, scale = 2)
    private BigDecimal costoReal;

    @Column(name = "fecha_hora_inicio_estimado")
    private LocalDateTime fechaHoraInicioEstimado;

    @Column(name = "fecha_hora_fin_estimado")
    private LocalDateTime fechaHoraFinEstimado;

    @Column(name = "fecha_hora_inicio_real")
    private LocalDateTime fechaHoraInicioReal;

    @Column(name = "fecha_hora_fin_real")
    private LocalDateTime fechaHoraFinReal;

    @Column(name = "orden", nullable = false)
    private Integer orden;

    @Column(name = "dominio_camion", length = 80)
    private String dominioCamion;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_ruta", nullable = false)
    private RutaTraslado rutaTraslado;

    @Column(name = "id_origen", nullable = false)
    private Long idOrigen;

    @Column(name = "id_destino", nullable = false)
    private Long idDestino;

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

    public boolean esUltimo(List<Tramo> tramos){
        return tramos.getLast().getIdTramo().equals(this.idTramo);
    }

    public boolean esPrimero(List<Tramo> tramos){
        return tramos.getFirst().getIdTramo().equals(this.idTramo);
    }

}

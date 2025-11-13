package backend.grupo130.tramos.data.models;

import backend.grupo130.tramos.client.camiones.models.Camion;
import backend.grupo130.tramos.client.ubicaciones.models.Ubicacion;
import backend.grupo130.tramos.config.enums.Estado;
import backend.grupo130.tramos.config.enums.TipoTramo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "tramos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tramo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tramo")
    private Integer idTramo;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_tramo", nullable = false)
    private TipoTramo tipoTramo;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private Estado estado;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_ruta", nullable = false)
    private RutaTraslado rutaTraslado;

    @Column(name = "id_origen", nullable = false)
    private Integer idOrigen;

    @Column(name = "id_destino", nullable = false)
    private Integer idDestino;

    // Dominio

    @Transient
    private Camion camion;

    @Transient
    private Ubicacion origen;

    @Transient
    private Ubicacion destino;

    public boolean esEstimado(){
        return estado.equals(Estado.ESTIMADO);
    }

    public boolean esAsignado() {
        return  estado.equals(Estado.ASIGNADO);
    }

    public boolean esIniciado() {
        return  estado.equals(Estado.INICIADO);
    }

    public boolean esFinalizado(){
        return  estado.equals(Estado.FINALZADO);
    }

    public boolean esUltimo(){
        return this.rutaTraslado.getTramos().getLast().equals(this);
    }

    public boolean esPrimero(){
        return this.rutaTraslado.getTramos().getFirst().equals(this);
    }

}

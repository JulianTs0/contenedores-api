package backend.grupo130.envios.data.models;

import backend.grupo130.envios.config.enums.EstadoSolicitud;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "solicitud_traslado")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SolicitudTrasladoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_solicitud")
    private Long idSolicitud;

    @Column(name = "fecha_inicio")
    private LocalDateTime fechaInicio;

    @Column(name = "fecha_fin")
    private LocalDateTime fechaFin;

    @Column(name = "tiempo_estimado_horas", precision = 10, scale = 2)
    private BigDecimal tiempoEstimadoHoras;

    @Column(name = "tiempo_real_horas", precision = 10, scale = 2)
    private BigDecimal tiempoRealHoras;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "id_tarifa", referencedColumnName = "id_tarifa")
    private TarifaModel tarifa;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoSolicitud estado;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_solicitud")
    private List<SeguimientoEnvioModel> seguimientos;

    @Column(name = "id_contenedor", nullable = false)
    private Long idContenedor;

    @Column(name = "id_cliente", nullable = false)
    private Long idCliente;

    @Column(name = "id_origen")
    private Long idOrigen;

    @Column(name = "id_destino")
    private Long idDestino;

}

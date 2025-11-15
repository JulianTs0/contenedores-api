package backend.grupo130.tramos.data.models;

import backend.grupo130.tramos.client.envios.models.SolicitudTraslado;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "ruta_traslado")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RutaTraslado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ruta")
    private Integer idRuta;

    @Column(name = "cantidad_tramos", nullable = false)
    private Integer cantidadTramos;

    @Column(name = "cantidad_depositos", nullable = false)
    private Integer cantidadDepositos;

    @Column(name = "cargos_gestion_fijo", precision = 10, scale = 2, nullable = false)
    private BigDecimal cargosGestionFijo;

    @Column(name = "id_solicitud", nullable = false)
    private Integer idSolicitud;

    @Transient
    private SolicitudTraslado solicitud;

}

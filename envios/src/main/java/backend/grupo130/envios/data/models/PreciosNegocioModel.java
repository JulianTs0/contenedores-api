package backend.grupo130.envios.data.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "precios_negocio")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PreciosNegocioModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_precios_negocio")
    private Long idPreciosNegocio;

    @Column(name = "rango_peso_bajo", precision = 15, scale = 2)
    private BigDecimal rangoPesoBajo;

    @Column(name = "rango_peso_medio", precision = 15, scale = 2)
    private BigDecimal rangoPesoMedio;

    @Column(name = "multiplicador_bajo", precision = 5, scale = 2)
    private BigDecimal multiplicadorBajo;

    @Column(name = "multiplicador_medio", precision = 5, scale = 2)
    private BigDecimal multiplicadorMedio;

    @Column(name = "multiplicador_alto", precision = 5, scale = 2)
    private BigDecimal multiplicadorAlto;

    @Column(name = "valor_litro", precision = 15, scale = 2)
    private BigDecimal valorLitro;

    @Column(name = "cargo_gestion", precision = 15, scale = 2)
    private BigDecimal cargoGestion;

    @CreationTimestamp
    @Column(name = "fecha_creacion", updatable = false, nullable = false)
    private LocalDateTime fechaCreacion;

    @UpdateTimestamp
    @Column(name = "fecha_actualizacion", nullable = false)
    private LocalDateTime fechaActualizacion;
}

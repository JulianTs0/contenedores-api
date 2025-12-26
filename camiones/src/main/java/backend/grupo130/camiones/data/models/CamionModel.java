package backend.grupo130.camiones.data.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "Camion")
@Getter
@Setter
@NoArgsConstructor
public class CamionModel {

    @Id
    @Column(name = "dominio", length = 80)
    private String dominio;

    @Column(name = "capacidad_peso", precision = 10, scale = 2, nullable = false)
    private BigDecimal capacidadPeso;

    @Column(name = "capacidad_volumen", precision = 10, scale = 2, nullable = false)
    private BigDecimal capacidadVolumen;

    @Column(name = "consumo_combustible", precision = 10, scale = 2, nullable = false)
    private BigDecimal consumoCombustible;

    @Column(name = "costo_traslado_base", precision = 10, scale = 2, nullable = false)
    private BigDecimal costoTrasladoBase;

    @Column(name = "estado", nullable = false)
    private Boolean estado;

    @Column(name = "id_transportista", unique = true)
    private Long idTransportista;

}

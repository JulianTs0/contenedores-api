package backend.grupo130.envios.data.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "tarifa")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TarifaModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tarifa")
    private Long idTarifa;

    @Column(name = "peso_max", precision = 10, scale = 2)
    private BigDecimal pesoMax;

    @Column(name = "volumen_max", precision = 10, scale = 2)
    private BigDecimal volumenMax;

    @Column(name = "costo_base", nullable = false, precision = 15, scale = 2)
    private BigDecimal costoBase;

    @Column(name = "valor_litro", nullable = false, precision = 15, scale = 2)
    private BigDecimal valorLitro;

    @Column(name = "consumo_aprox", precision = 15, scale = 2)
    private BigDecimal consumoAprox;

    @Column(name = "costo_estadia", nullable = false, precision = 15, scale = 2)
    private BigDecimal costoEstadia;

    @Column(name = "costo_estimado", precision = 15, scale = 2)
    private BigDecimal costoEstimado;

    @Column(name = "costo_final", precision = 15, scale = 2)
    private BigDecimal costoFinal;

}

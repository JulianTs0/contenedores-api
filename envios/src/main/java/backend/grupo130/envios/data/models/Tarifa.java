package backend.grupo130.envios.data.models;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tarifa")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Tarifa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tarifa")
    private Long idTarifa;

    @Column(name = "volumen_min", precision = 10, scale = 2)
    private BigDecimal volumenMin;

    @Column(name = "volumen_max", precision = 10, scale = 2)
    private BigDecimal volumenMax;

    @Column(name = "costo_base", nullable = false, precision = 10, scale = 2)
    private BigDecimal costoBase;

    @Column(name = "valor_litro", nullable = false, precision = 10, scale = 2)
    private BigDecimal valorLitro;

    @Column(name = "consumo_aprox", precision = 10, scale = 2)
    private BigDecimal consumoAprox;

    @Column(name = "costo_estadia", nullable = false, precision = 10, scale = 2)
    private BigDecimal costoEstadia;

}

package backend.grupo130.ubicaciones.data.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "Deposito")
@Getter
@Setter
@NoArgsConstructor
public class DepositoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_deposito")
    private Long idDeposito;

    @Column(name = "nombre", nullable = false, length = 30)
    private String nombre;

    @Column(name = "costo_estadia_diario", nullable = false, precision = 10, scale = 2)
    private BigDecimal costoEstadiaDiario;

}

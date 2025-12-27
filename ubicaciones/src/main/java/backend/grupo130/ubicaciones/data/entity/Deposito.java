package backend.grupo130.ubicaciones.data.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Deposito {

    private Long idDeposito;

    private String nombre;

    private BigDecimal costoEstadiaDiario;

}

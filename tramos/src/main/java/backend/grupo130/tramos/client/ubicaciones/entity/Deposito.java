package backend.grupo130.tramos.client.ubicaciones.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Deposito {

    private Long idDeposito;

    private String nombre;

    private BigDecimal costoEstadiaDiario;

}

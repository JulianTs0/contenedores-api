package backend.grupo130.tramos.client.envios.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tarifa {

    private Integer idTarifa;

    private BigDecimal volumenMin;

    private BigDecimal volumenMax;

    private BigDecimal costoBase;

    private BigDecimal valorLitro;

    private BigDecimal consumoAprox;

    private BigDecimal costoEstadia;

}

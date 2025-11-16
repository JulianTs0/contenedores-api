package backend.grupo130.envios.dto.tarifa.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class TarifaGetByIdResponse {

    private final Long idTarifa;
    private final BigDecimal pesoMax;
    private final BigDecimal volumenMax;
    private final BigDecimal costoBase;
    private final BigDecimal valorLitro;
    private final BigDecimal consumoAprox;
    private final BigDecimal costoEstadia;
}

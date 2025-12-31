package backend.grupo130.envios.dto.precios.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PreciosNegocioUpdateResponse {

    private final Long idPreciosNegocio;

    private final BigDecimal rangoPesoBajo;

    private final BigDecimal rangoPesoMedio;

    private final BigDecimal multiplicadorBajo;

    private final BigDecimal multiplicadorMedio;

    private final BigDecimal multiplicadorAlto;

    private final BigDecimal valorLitro;

    private final BigDecimal cargoGestion;
}

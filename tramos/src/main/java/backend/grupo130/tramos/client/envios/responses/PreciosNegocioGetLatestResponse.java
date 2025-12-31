package backend.grupo130.tramos.client.envios.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PreciosNegocioGetLatestResponse {

    private Long idPreciosNegocio;

    private BigDecimal rangoPesoBajo;

    private BigDecimal rangoPesoMedio;

    private BigDecimal multiplicadorBajo;

    private BigDecimal multiplicadorMedio;

    private BigDecimal multiplicadorAlto;

    private BigDecimal valorLitro;

    private BigDecimal cargoGestion;

}

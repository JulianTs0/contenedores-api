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
public class PreciosNegocioCreateResponse {

    private final Long idPreciosNegocio;

}

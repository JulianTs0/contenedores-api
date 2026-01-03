package backend.grupo130.envios.dto.precios.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
public class PreciosNegocioCreateResponse {

    private final Long idPreciosNegocio;

}

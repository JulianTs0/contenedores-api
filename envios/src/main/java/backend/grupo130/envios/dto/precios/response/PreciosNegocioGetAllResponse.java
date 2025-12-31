package backend.grupo130.envios.dto.precios.response;

import backend.grupo130.envios.data.entity.PreciosNegocio;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class PreciosNegocioGetAllResponse {

    private final List<PreciosNegocio> preciosNegocios;

}

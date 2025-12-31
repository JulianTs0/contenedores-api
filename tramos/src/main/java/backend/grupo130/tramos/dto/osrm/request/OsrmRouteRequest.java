package backend.grupo130.tramos.dto.osrm.request;

import backend.grupo130.tramos.client.ubicaciones.entity.Ubicacion;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OsrmRouteRequest {

    private final Ubicacion origen;

    private final Ubicacion destino;

    private final Integer alternativa;

}

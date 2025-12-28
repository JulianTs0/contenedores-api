package backend.grupo130.tramos.external.OSRM;

import backend.grupo130.tramos.client.ubicaciones.entity.Ubicacion;
import backend.grupo130.tramos.config.exceptions.ServiceError;
import backend.grupo130.tramos.external.OSRM.response.OsrmRouteResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class OsrmApiClient {

    private final RestClient osrmClient;

    public OsrmRouteResponse calcularDistancia(Ubicacion origen, Ubicacion destino) throws ServiceError {


        final String PUBLIC_OSRM_URL = "http://router.project-osrm.org/route/v1/driving/{0};{1}";

        String origenCoords = origen.getLongitud() + "," + origen.getLatitud();
        String destinoCoords = destino.getLongitud() + "," + destino.getLatitud();

        OsrmRouteResponse response = osrmClient.get()
            .uri(PUBLIC_OSRM_URL, origenCoords, destinoCoords)
            .retrieve()
            .body(OsrmRouteResponse.class);

        return response;
    }

}

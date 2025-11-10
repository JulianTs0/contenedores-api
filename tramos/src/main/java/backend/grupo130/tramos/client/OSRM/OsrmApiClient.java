package backend.grupo130.tramos.client.OSRM;

import backend.grupo130.tramos.client.OSRM.response.OsrmRouteResponse;
import backend.grupo130.tramos.client.ubicaciones.models.Ubicacion;
import backend.grupo130.tramos.config.exceptions.ServiceError;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class OsrmApiClient {

    private final RestClient osrmClient;

    public OsrmRouteResponse calcularDistancia(Ubicacion origen, Ubicacion destino) throws ServiceError {
        try {
            final String URI_TEMPLATE = "${spring.clients.osrm.uri-template}";

            String origenCoords = origen.getLongitud() + "," + origen.getLatitud();
            String destinoCoords = destino.getLongitud() + "," + destino.getLatitud();

            OsrmRouteResponse response = osrmClient.get()
                .uri(URI_TEMPLATE, origenCoords, destinoCoords)
                .retrieve()
                .body(OsrmRouteResponse.class);

            return response;
        } catch (ServiceError ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ServiceError("Error interno", 500);
        }
    }

}

package backend.grupo130.tramos.external;

import backend.grupo130.tramos.config.enums.Errores;
import backend.grupo130.tramos.config.exceptions.ServiceError;
import backend.grupo130.tramos.dto.osrm.OsrmMapperDto;
import backend.grupo130.tramos.dto.osrm.request.OsrmRouteRequest;
import backend.grupo130.tramos.dto.osrm.response.OsrmApiResponse;
import backend.grupo130.tramos.dto.osrm.response.OsrmRouteResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class OsrmApiClient {

    private final RestClient osrmClient;

    @Value("${spring.clients.osrm.uri-template}")
    private String PUBLIC_OSRM_URL;

    private int normalizarAlternativas(Integer alternativa){
        return (alternativa == null || alternativa < 1) ? 1 : alternativa;
    }

    public OsrmRouteResponse calcularDistancia(OsrmRouteRequest request) throws ServiceError {

        String origenCoords = request.getOrigen().getLongitud() + "," + request.getOrigen().getLatitud();
        String destinoCoords = request.getDestino().getLongitud() + "," + request.getDestino().getLatitud();

        int index = normalizarAlternativas(request.getAlternativa());

        String urlWithParams = this.PUBLIC_OSRM_URL + ( (index > 1) ? "?alternatives=true" : "");

        OsrmApiResponse apiResponse = this.osrmClient.get()
            .uri(urlWithParams, origenCoords, destinoCoords)
            .retrieve()
            .body(OsrmApiResponse.class);

        if (apiResponse == null || apiResponse.getRoutes() == null){
            throw new ServiceError("", Errores.ERROR_API, 500);
        }

        if (!apiResponse.getRoutes().isEmpty()) {
            int routeIndex = index - 1;

            var rutaSeleccionada = (routeIndex < apiResponse.getRoutes().size())
                ? apiResponse.getRoutes().get(routeIndex)
                : apiResponse.getRoutes().getFirst();

            return OsrmMapperDto.toResponse(rutaSeleccionada);
        }

        return OsrmMapperDto.toResponse(apiResponse.getRoutes().getFirst());
    }

}

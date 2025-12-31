package backend.grupo130.tramos.dto.osrm;


import backend.grupo130.tramos.client.ubicaciones.entity.Ubicacion;
import backend.grupo130.tramos.dto.osrm.request.OsrmRouteRequest;
import backend.grupo130.tramos.dto.osrm.response.OsrmRouteResponse;
import backend.grupo130.tramos.dto.osrm.response.RouteResponse;

public class OsrmMapperDto {

    public static OsrmRouteResponse toResponse(RouteResponse ruta) {
        return new OsrmRouteResponse(
            ruta
        );
    }

    public static OsrmRouteRequest toRequest(Ubicacion origen, Ubicacion destino, Integer alternativa) {
        return new OsrmRouteRequest(
            origen,
            destino,
            alternativa
        );
    }

}

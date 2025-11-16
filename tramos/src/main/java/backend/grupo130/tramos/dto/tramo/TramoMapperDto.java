package backend.grupo130.tramos.dto.tramo;

import backend.grupo130.tramos.client.camiones.models.Camion;
import backend.grupo130.tramos.client.ubicaciones.models.Ubicacion;
import backend.grupo130.tramos.data.models.RutaTraslado;
import backend.grupo130.tramos.data.models.Tramo;
import backend.grupo130.tramos.dto.tramo.response.TramoGetAllResponse;
import backend.grupo130.tramos.dto.tramo.response.TramoGetByIdResponse;

import java.util.List;

public class TramoMapperDto {

    public static TramoGetByIdResponse toResponseGet(Tramo tramo, Camion camion, Ubicacion origen, Ubicacion destino) {
        return new TramoGetByIdResponse(
            tramo.getIdTramo(),
            tramo.getTipoTramo().name(),
            tramo.getEstado().name(),
            tramo.getCostoAproximado(),
            tramo.getCostoReal(),
            tramo.getFechaHoraInicioEstimado(),
            tramo.getFechaHoraFinEstimado(),
            tramo.getFechaHoraInicioReal(),
            tramo.getFechaHoraFinReal(),
            tramo.getOrden(),
            camion,
            tramo.getRutaTraslado(),
            origen,
            destino
        );
    }

    public static TramoGetAllResponse toResponseGet(List<Tramo> tramos) {

        return new TramoGetAllResponse(
            tramos
        );
    }

}

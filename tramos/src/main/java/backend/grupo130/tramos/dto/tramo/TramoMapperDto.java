package backend.grupo130.tramos.dto.tramo;

import backend.grupo130.tramos.client.camiones.entity.Camion;
import backend.grupo130.tramos.client.ubicaciones.entity.Ubicacion;
import backend.grupo130.tramos.data.entity.Tramo;
import backend.grupo130.tramos.dto.tramo.request.*;
import backend.grupo130.tramos.dto.tramo.response.TramoGetAllResponse;
import backend.grupo130.tramos.dto.tramo.response.TramoGetByIdResponse;

import java.util.List;

public class TramoMapperDto {

    public static TramoGetByIdResponse toResponseGetById(Tramo tramo, Camion camion, Ubicacion origen, Ubicacion destino) {
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

    public static TramoAsignacionCamionRequest toRequestPatchCamion(Long id, TramoAsignacionCamionRequest body) {
        return TramoAsignacionCamionRequest.builder()
            .idTramo(id)
            .dominioCamion(body.getDominioCamion())
            .build();
    }

    public static TramoInicioTramoRequest toRequestPatchInicio(Long id, TramoInicioTramoRequest body) {
        return TramoInicioTramoRequest.builder()
            .idTramo(id)
            .dominioCamion(body.getDominioCamion())
            .build();
    }

    public static TramoFinTramoRequest toRequestPatchFin(Long id, TramoFinTramoRequest body) {
        return TramoFinTramoRequest.builder()
            .idTramo(id)
            .dominioCamion(body.getDominioCamion())
            .build();
    }

    public static TramoGetAllResponse toResponseGetAll(List<Tramo> tramos) {
        return new TramoGetAllResponse(
            tramos
        );
    }

    public static TramoGetByRutaIdRequest toRequestGetRuta(Long id) {
        return TramoGetByRutaIdRequest.builder()
            .idRuta(id)
            .build();
    }

    public static TramoGetByTransportistaRequest toRequestGetTransportista(String dominio) {
        return TramoGetByTransportistaRequest.builder()
            .dominioCamion(dominio)
            .build();
    }

}

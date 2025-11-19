package backend.grupo130.envios.dto.solicitud;

import backend.grupo130.envios.data.models.SeguimientoEnvio;
import backend.grupo130.envios.data.models.SolicitudTraslado;
import backend.grupo130.envios.dto.solicitud.response.SolicitudCambioDeEstadoResponse;
import backend.grupo130.envios.dto.solicitud.response.SolicitudEditResponse;
import backend.grupo130.envios.dto.solicitud.response.SolicitudGetAllResponse;
import backend.grupo130.envios.dto.solicitud.response.SolicitudGetByIdResponse;

import java.util.List;

public class SolicitudMapperDto {

    public static SolicitudGetByIdResponse toResponseGet(SolicitudTraslado solicitud) {
        return new SolicitudGetByIdResponse(
                solicitud.getIdSolicitud(),
                solicitud.getFechaInicio(),
                solicitud.getFechaFin(),
                solicitud.getEstado().toString(), // Asegurado el .toString() o .name() según tu enum
                solicitud.getTarifa(),
                solicitud.getSeguimientos(),
                solicitud.getIdContenedor(),
                solicitud.getIdCliente(),
                solicitud.getIdOrigen(),
                solicitud.getIdDestino(),
                solicitud.getCostoEstimado(),
                solicitud.getCostoFinal(),
                solicitud.getTiempoEstimadoHoras(),
                solicitud.getTiempoRealHoras()
        );
    }

    public static SolicitudGetAllResponse toResponseGet(List<SolicitudTraslado> solicitudes) {
        return new SolicitudGetAllResponse(solicitudes);
    }

    public static SolicitudEditResponse toResponsePatch(SolicitudTraslado solicitud) {
        return new SolicitudEditResponse(
                solicitud.getIdSolicitud(),
                solicitud.getEstado().name(),
                solicitud.getFechaInicio(),
                solicitud.getFechaFin(),
                solicitud.getCostoEstimado(),
                solicitud.getCostoFinal(),
                solicitud.getTiempoEstimadoHoras(),
                solicitud.getTiempoRealHoras(),
                solicitud.getTarifa(),
                solicitud.getIdOrigen(),
                solicitud.getIdDestino()
        );
    }

    public static SolicitudCambioDeEstadoResponse toResponsePatch(SolicitudTraslado solicitud, SeguimientoEnvio nuevoSeguimiento) {
        return new SolicitudCambioDeEstadoResponse(
                solicitud.getIdSolicitud(),
                solicitud.getEstado().name(),
                nuevoSeguimiento.getIdSeguimiento()
        );
    }
}
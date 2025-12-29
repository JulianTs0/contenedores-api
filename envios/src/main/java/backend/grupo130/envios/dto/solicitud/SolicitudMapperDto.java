package backend.grupo130.envios.dto.solicitud;

import backend.grupo130.envios.data.entity.SolicitudTraslado;
import backend.grupo130.envios.data.entity.SeguimientoEnvio;
import backend.grupo130.envios.dto.solicitud.request.SolicitudCambioDeEstadoRequest;
import backend.grupo130.envios.dto.solicitud.request.SolicitudEditRequest;
import backend.grupo130.envios.dto.solicitud.response.SolicitudCambioDeEstadoResponse;
import backend.grupo130.envios.dto.solicitud.response.SolicitudEditResponse;
import backend.grupo130.envios.dto.solicitud.response.SolicitudGetAllResponse;
import backend.grupo130.envios.dto.solicitud.response.SolicitudGetByIdResponse;

import java.util.List;

public class SolicitudMapperDto {

    public static SolicitudGetByIdResponse toSolicitudGetByIdResponse(SolicitudTraslado solicitud) {
        return new SolicitudGetByIdResponse(
            solicitud.getIdSolicitud(),
            solicitud.getFechaInicio(),
            solicitud.getFechaFin(),
            solicitud.getEstado().toString(),
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

    public static SolicitudGetAllResponse toSolicitudGetAllResponse(List<SolicitudTraslado> solicitudes) {
        return new SolicitudGetAllResponse(solicitudes);
    }

    public static SolicitudEditResponse toSolicitudEditResponse(SolicitudTraslado solicitud) {
        return new SolicitudEditResponse(
            solicitud.getIdSolicitud(),
            solicitud.getFechaInicio(),
            solicitud.getFechaFin(),
            solicitud.getEstado().toString(),
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

    public static SolicitudCambioDeEstadoResponse toSolicitudCambioDeEstadoResponse(SolicitudTraslado solicitud, SeguimientoEnvio nuevoSeguimiento) {
        return new SolicitudCambioDeEstadoResponse(
            solicitud.getIdSolicitud(),
            solicitud.getEstado().name(),
            nuevoSeguimiento.getIdSeguimiento()
        );
    }

    public static SolicitudEditRequest toSolicitudEditRequest(Long id, SolicitudEditRequest body) {
        return SolicitudEditRequest.builder()
            .idSolicitud(id)
            .fechaInicio(body.getFechaInicio())
            .fechaFin(body.getFechaFin())
            .costoEstimado(body.getCostoEstimado())
            .costoFinal(body.getCostoFinal())
            .tiempoEstimadoHoras(body.getTiempoEstimadoHoras())
            .tiempoRealHoras(body.getTiempoRealHoras())
            .tarifa(body.getTarifa())
            .idOrigen(body.getIdOrigen())
            .idDestino(body.getIdDestino())
            .build();
    }

    public static SolicitudCambioDeEstadoRequest toSolicitudCambioDeEstadoRequest(Long id, SolicitudCambioDeEstadoRequest body) {
        return SolicitudCambioDeEstadoRequest.builder()
            .idSolicitud(id)
            .nuevoEstado(body.getNuevoEstado())
            .descripcion(body.getDescripcion())
            .build();
    }
}

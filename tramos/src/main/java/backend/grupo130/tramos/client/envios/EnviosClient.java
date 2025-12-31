package backend.grupo130.tramos.client.envios;

import backend.grupo130.tramos.client.envios.entity.PreciosNegocio;
import backend.grupo130.tramos.client.envios.entity.SolicitudTraslado;
import backend.grupo130.tramos.client.envios.request.SolicitudCambioDeEstadoRequest;
import backend.grupo130.tramos.client.envios.request.SolicitudEditRequest;
import backend.grupo130.tramos.client.envios.responses.PreciosNegocioGetLatestResponse;
import backend.grupo130.tramos.client.envios.responses.SolicitudCambioDeEstadoResponse;
import backend.grupo130.tramos.client.envios.responses.SolicitudEditResponse;
import backend.grupo130.tramos.client.envios.responses.SolicitudGetByIdResponse;
import backend.grupo130.tramos.config.enums.EstadoSolicitud;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
@Slf4j
public class EnviosClient {

    private final EnviosGateway enviosGateway;

    public SolicitudTraslado getSolicitudTrasladoById(Long solicitudTrasladoId) {

        SolicitudGetByIdResponse response = this.enviosGateway.getSolicitudTrasladoById(solicitudTrasladoId);

        log.warn(response.getEstado());

        return new SolicitudTraslado(
            response.getIdSolicitud(),
            response.getFechaInicio(),
            response.getFechaFin(),
            response.getTiempoEstimadoHoras(),
            response.getTiempoRealHoras(),
            response.getCostoEstimado(),
            response.getCostoFinal(),
            EstadoSolicitud.fromString(response.getEstado()),
            response.getTarifa(),
            response.getSeguimientos(),
            response.getIdContenedor(),
            response.getIdCliente(),
            response.getIdOrigen(),
            response.getIdDestino()
        );

    }

    public SolicitudEditResponse editSolicitud(Long id, SolicitudEditRequest request) {

        return this.enviosGateway.editSolicitud(id, request);
    }


    public SolicitudCambioDeEstadoResponse cambioDeEstadoSolicitud(Long id, SolicitudCambioDeEstadoRequest request) {
        return this.enviosGateway.cambioDeEstadoSolicitud(id, request);
    }

    public PreciosNegocio getUltimosPrecios() {
        PreciosNegocioGetLatestResponse response = this.enviosGateway.getUltimo();
        return new PreciosNegocio(
            response.getIdPreciosNegocio(),
            response.getRangoPesoBajo(),
            response.getRangoPesoMedio(),
            response.getMultiplicadorBajo(),
            response.getMultiplicadorMedio(),
            response.getMultiplicadorAlto(),
            response.getValorLitro(),
            response.getCargoGestion()
        );
    }
}

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
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import static net.logstash.logback.argument.StructuredArguments.kv;

@Component
@AllArgsConstructor
@Slf4j
public class EnviosClient {

    private final EnviosGateway enviosGateway;

    public SolicitudTraslado getSolicitudTrasladoById(Long solicitudTrasladoId) {

        SolicitudGetByIdResponse response = this.enviosGateway.getSolicitudTrasladoById(solicitudTrasladoId);

        log.warn("Estado de la solicitud", 
            kv("estado", response.getEstado())
        );

        return new SolicitudTraslado(
            response.getIdSolicitud(),
            response.getFechaInicio(),
            response.getFechaFin(),
            response.getTiempoEstimadoHoras(),
            response.getTiempoRealHoras(),
            EstadoSolicitud.fromString(response.getEstado()),
            response.getTarifa(),
            response.getSeguimientos(),
            response.getIdContenedor(),
            response.getIdCliente(),
            response.getIdOrigen(),
            response.getIdDestino()
        );

    }

    public SolicitudEditResponse editSolicitud(Long id, SolicitudEditRequest body) {

        return this.enviosGateway.editSolicitud(id, body);
    }


    public SolicitudCambioDeEstadoResponse cambioDeEstadoSolicitud(
        Long id,
        String nuevoEstado,
        String descripcion
    ) {
        SolicitudCambioDeEstadoRequest body = SolicitudCambioDeEstadoRequest.builder()
            .nuevoEstado(nuevoEstado)
            .descripcion(descripcion)
            .build();

        return this.enviosGateway.cambioDeEstadoSolicitud(id, body);
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

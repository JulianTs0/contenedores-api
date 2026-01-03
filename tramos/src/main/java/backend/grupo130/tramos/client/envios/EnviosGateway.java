package backend.grupo130.tramos.client.envios;

import backend.grupo130.tramos.client.envios.request.SolicitudCambioDeEstadoRequest;
import backend.grupo130.tramos.client.envios.request.SolicitudEditRequest;
import backend.grupo130.tramos.client.envios.responses.PreciosNegocioGetLatestResponse;
import backend.grupo130.tramos.client.envios.responses.SolicitudCambioDeEstadoResponse;
import backend.grupo130.tramos.client.envios.responses.SolicitudEditResponse;
import backend.grupo130.tramos.client.envios.responses.SolicitudGetByIdResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "envios", url = "${spring.clients.envios.url}")
public interface EnviosGateway {

    @GetMapping("/solicitud/{id}")
    SolicitudGetByIdResponse getSolicitudTrasladoById(
        @PathVariable("id") Long id
    );

    @PatchMapping("/solicitud/{id}")
    SolicitudEditResponse editSolicitud(
        @PathVariable("id") Long id,
        @RequestBody SolicitudEditRequest request
    );

    @PatchMapping("/solicitud/{id}/estado")
    SolicitudCambioDeEstadoResponse cambioDeEstadoSolicitud(
        @PathVariable("id") Long id,
        @RequestBody SolicitudCambioDeEstadoRequest request
    );

    @GetMapping("/precios/ultimo")
    PreciosNegocioGetLatestResponse getUltimo();

}

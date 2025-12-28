package backend.grupo130.tramos.client.envios;

import backend.grupo130.tramos.client.envios.request.SolicitudCambioDeEstadoRequest;
import backend.grupo130.tramos.client.envios.request.SolicitudEditRequest;
import backend.grupo130.tramos.client.envios.responses.SolicitudCambioDeEstadoResponse;
import backend.grupo130.tramos.client.envios.responses.SolicitudEditResponse;
import backend.grupo130.tramos.client.envios.responses.SolicitudGetByIdResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "envios", url = "${spring.clients.envios.url}")
public interface EnvioGateway {

    @GetMapping("/solicitud/getById/{id}")
    SolicitudGetByIdResponse getSolicitudTrasladoById(@PathVariable("id") Long id);

    @PutMapping("/solicitud/edit")
    SolicitudEditResponse editSolicitud(@RequestBody SolicitudEditRequest request);

    @PutMapping("/solicitud/cambioDeEstado")
    SolicitudCambioDeEstadoResponse cambioDeEstadoSolicitud(@RequestBody SolicitudCambioDeEstadoRequest request);

}

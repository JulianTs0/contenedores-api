package backend.grupo130.tramos.client.envios;

import backend.grupo130.tramos.client.envios.request.SolicitudCambioDeEstadoRequest;
import backend.grupo130.tramos.client.envios.request.SolicitudEditRequest;
import backend.grupo130.tramos.client.envios.responses.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "envios", url = "${spring.clients.envios.url}")
public interface EnvioClient {

    @GetMapping("/tarifa/getById/{id}")
    GetTarifaByIdResponse getTarifaById(@PathVariable("id") Long id);

    @GetMapping("/solicitud/getById/{id}")
    SolicitudGetByIdResponse getSolicitudTrasladoById(@PathVariable("id") Long id);

    @GetMapping("/seguimientoEnvio/getById/{id}")
    GetSeguimientoEnvioByIdResponse getSeguimientoEnvioById(@PathVariable("id") Long id);

    @PostMapping("/solicitud/edit")
    SolicitudEditResponse editSolicitud(@RequestBody SolicitudEditRequest request);

    @PostMapping("/solicitud/cambioDeEstado")
    SolicitudCambioDeEstadoResponse cambioDeEstadoSolicitud(@RequestBody SolicitudCambioDeEstadoRequest request);

}

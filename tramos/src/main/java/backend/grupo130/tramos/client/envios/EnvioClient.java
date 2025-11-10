package backend.grupo130.tramos.client.envios;

import backend.grupo130.tramos.client.envios.responses.GetTarifaByIdResponse;
import backend.grupo130.tramos.client.envios.responses.GetSolicitudTrasladoByIdResponse;
import backend.grupo130.tramos.client.envios.responses.GetSeguimientoEnvioByIdResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "envios", url = "${spring.clients.envios.url}")
public interface EnvioClient {

    @GetMapping("/tarifa/getById/{id}")
    GetTarifaByIdResponse getTarifaById(@PathVariable("id") Integer id);

    @GetMapping("/solicitudTraslado/getById/{id}")
    GetSolicitudTrasladoByIdResponse getSolicitudTrasladoById(@PathVariable("id") Integer id);

    @GetMapping("/seguimientoEnvio/getById/{id}")
    GetSeguimientoEnvioByIdResponse getSeguimientoEnvioById(@PathVariable("id") Integer id);

}

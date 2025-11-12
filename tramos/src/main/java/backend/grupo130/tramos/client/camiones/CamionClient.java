package backend.grupo130.tramos.client.camiones;

import backend.grupo130.tramos.client.camiones.responses.GetCamionByIdResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "camiones", url = "${spring.clients.camiones.url}")
public interface CamionClient {

    @GetMapping("/getById/{dominio}")
    GetCamionByIdResponse getBYId(@PathVariable("dominio") String dominio);

}

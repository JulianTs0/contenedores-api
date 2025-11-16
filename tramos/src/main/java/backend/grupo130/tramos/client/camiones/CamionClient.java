package backend.grupo130.tramos.client.camiones;

import backend.grupo130.tramos.client.camiones.request.GetOpcionesCamionesRequest;
import backend.grupo130.tramos.client.camiones.responses.GetAllResponse;
import backend.grupo130.tramos.client.camiones.responses.GetCamionByIdResponse;
import backend.grupo130.tramos.client.camiones.responses.GetPromedioCombustibleActualResponse;
import backend.grupo130.tramos.client.camiones.responses.GetPromedioCostoBaseResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "camiones", url = "${spring.clients.camiones.url}")
public interface CamionClient {

    @GetMapping("/getById/{dominio}")
    GetCamionByIdResponse getBYId(@PathVariable("dominio") String dominio);

    @PostMapping("/getOpciones")
    GetPromedioCostoBaseResponse getPromedioCostoBase(@RequestBody GetOpcionesCamionesRequest request);

    @GetMapping("/getConsumoPromedio")
    GetPromedioCombustibleActualResponse getConsumoPromedio();

}

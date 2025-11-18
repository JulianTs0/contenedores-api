package backend.grupo130.tramos.client.camiones;

import backend.grupo130.tramos.client.camiones.request.CambiarDisponibilidadRequest;
import backend.grupo130.tramos.client.camiones.request.GetOpcionesCamionesRequest;
import backend.grupo130.tramos.client.camiones.responses.*;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "camiones", url = "${spring.clients.camiones.url}")
public interface CamionClient {

    @GetMapping("/getById/{dominio}")
    GetCamionByIdResponse getBYId(@PathVariable("dominio") String dominio);

    @PostMapping("/getOpciones")
    GetPromedioCostoBaseResponse getPromedioCostoBase(@RequestBody GetOpcionesCamionesRequest request);

    @GetMapping("/getConsumoPromedio")
    GetPromedioCombustibleActualResponse getConsumoPromedio();

    @PutMapping("/cambiarDisponibilidad")
    EditResponse cambiarDisponibilidad(@RequestBody CambiarDisponibilidadRequest request);

}

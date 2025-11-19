package backend.grupo130.tramos.client.camiones;

import backend.grupo130.tramos.client.camiones.request.CamionCambiarDisponibilidadRequest;
import backend.grupo130.tramos.client.camiones.responses.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@FeignClient(name = "camiones", url = "${spring.clients.camiones.url}")
public interface CamionClient {

    @GetMapping("/getById/{dominio}")
    CamionGetCamionByIdResponse getBYId(@PathVariable("dominio") String dominio);

    @GetMapping("/getCostoPromedio")
    CamionGetPromedioCostoBaseResponse getCostoPromedio(
        @RequestParam(value = "capacidadPeso") BigDecimal capacidadPeso,
        @RequestParam(value = "capacidadVolumen") BigDecimal capacidadVolumen
    );

    @GetMapping("/getConsumoPromedio")
    CamionGetPromedioCombustibleActualResponse getConsumoPromedio();

    @PutMapping("/cambiarDisponibilidad")
    CamionEditResponse cambiarDisponibilidad(@RequestBody CamionCambiarDisponibilidadRequest request);

}

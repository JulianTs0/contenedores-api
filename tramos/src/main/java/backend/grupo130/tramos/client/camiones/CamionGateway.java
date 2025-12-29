package backend.grupo130.tramos.client.camiones;

import backend.grupo130.tramos.client.camiones.responses.CamionEditResponse;
import backend.grupo130.tramos.client.camiones.responses.CamionGetCamionByIdResponse;
import backend.grupo130.tramos.client.camiones.responses.CamionGetPromedioCombustibleActualResponse;
import backend.grupo130.tramos.client.camiones.responses.CamionGetPromedioCostoBaseResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@FeignClient(name = "camiones", url = "${spring.clients.camiones.url}")
public interface CamionGateway {

    @GetMapping("/{dominio}")
    CamionGetCamionByIdResponse getById(
        @PathVariable("dominio") String dominio
    );

    @GetMapping("/costo/promedio")
    CamionGetPromedioCostoBaseResponse getCostoPromedio(
        @RequestParam(value = "peso") BigDecimal capacidadPeso,
        @RequestParam(value = "volumen") BigDecimal capacidadVolumen
    );

    @GetMapping("/consumo/promedio")
    CamionGetPromedioCombustibleActualResponse getConsumoPromedio();

    @PutMapping("/disponibilidad/{dominio}")
    CamionEditResponse cambiarDisponibilidad(
        @PathVariable String dominio,
        @RequestBody Map<String, Object> body
    );

}

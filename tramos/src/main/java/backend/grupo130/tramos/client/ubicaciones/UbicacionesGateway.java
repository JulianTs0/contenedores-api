package backend.grupo130.tramos.client.ubicaciones;

import backend.grupo130.tramos.client.ubicaciones.responses.DepositoGetByIdResponse;
import backend.grupo130.tramos.client.ubicaciones.responses.UbicacionGetAllResponse;
import backend.grupo130.tramos.client.ubicaciones.responses.UbicacionGetByIdResponse;
import backend.grupo130.tramos.client.ubicaciones.responses.UbicacionGetListByIdResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "ubicaciones", url = "${spring.clients.ubicaciones.url}")
public interface UbicacionesGateway {

    @GetMapping("/depositos/{id}")
    DepositoGetByIdResponse getDepositoById(
        @PathVariable("id") Long id
    );

    @GetMapping("/ubicaciones/{id}")
    UbicacionGetByIdResponse getUbicacionById(
        @PathVariable("id") Long id
    );

    @GetMapping("/ubicaciones/")
    UbicacionGetAllResponse getUbicacionAll();

    @GetMapping("/ubicaciones/lista")
    UbicacionGetListByIdResponse getUbicacionByIds(
        @RequestParam(value = "ids") List<Long> ids
    );

}

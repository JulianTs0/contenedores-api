package backend.grupo130.tramos.client.ubicaciones;

import backend.grupo130.tramos.client.ubicaciones.responses.GetDepositoByIdResponse;
import backend.grupo130.tramos.client.ubicaciones.responses.GetUbicacionByIdResponse;
import backend.grupo130.tramos.client.ubicaciones.responses.GetUbicacionGetAllResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ubicaciones", url = "${spring.clients.ubicaciones.url}")
public interface UbicacionClient {

    @GetMapping("/depositos/getById/{id}")
    GetDepositoByIdResponse getDepositoById(@PathVariable("id") Integer id);

    @GetMapping("/ubicaciones/getById/{id}")
    GetUbicacionByIdResponse getUbicacionById(@PathVariable("id") Integer id);

    @GetMapping("/ubicaciones/getAll/{id}")
    GetUbicacionGetAllResponse getUbicacionAll();

}

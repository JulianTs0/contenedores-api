package backend.grupo130.envios.client.contenedores;

import backend.grupo130.envios.client.contenedores.request.ContenedorAsignarClienteRequest;
import backend.grupo130.envios.client.contenedores.request.ContenedorRegisterRequest;
import backend.grupo130.envios.client.contenedores.responses.ContenedorGetByIdResponse;
import backend.grupo130.envios.client.contenedores.responses.ContenedorGetByPesoVolumenResponse;
import backend.grupo130.envios.client.contenedores.responses.ContenedorRegisterResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@FeignClient(name = "contenedores", url = "${spring.clients.contenedores.url}")
public interface ContenedorGateway {

    @GetMapping("/{id}")
    ContenedorGetByIdResponse getBYId(
        @PathVariable("id") Long id
    );

    @GetMapping("/capacidad")
    ContenedorGetByPesoVolumenResponse getByPesoVolumen(
        @RequestParam(value = "peso") BigDecimal peso,
        @RequestParam(value = "volumen") BigDecimal volumen
    );

    @PostMapping("/")
    ContenedorRegisterResponse register(
        @RequestBody ContenedorRegisterRequest request
    );

    @PatchMapping("/cliente/{id}")
    void asignarCliente(
        @PathVariable("id") Long id,
        @RequestBody ContenedorAsignarClienteRequest request
    );

}

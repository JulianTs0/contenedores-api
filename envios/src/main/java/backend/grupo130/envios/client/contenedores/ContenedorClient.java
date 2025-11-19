package backend.grupo130.envios.client.contenedores;

import backend.grupo130.envios.client.contenedores.request.ContenedorAsignarClienteRequest;
import backend.grupo130.envios.client.contenedores.request.ContenedorRegisterRequest;
import backend.grupo130.envios.client.contenedores.responses.ContenedorGetByIdResponse;
import backend.grupo130.envios.client.contenedores.responses.ContenedorGetByPesoVolumenResponse;
import backend.grupo130.envios.client.contenedores.responses.ContenedorRegisterResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@FeignClient(name = "contenedores", url = "${spring.clients.contenedores.url}")
public interface ContenedorClient {

    @GetMapping("/getById/{id}")
    ContenedorGetByIdResponse getBYId(@PathVariable("id") Long id);

    @GetMapping("/getByPesoVolumen")
    ContenedorGetByPesoVolumenResponse getByPesoVolumen(
        @RequestParam(value = "capacidadPeso") BigDecimal peso,
        @RequestParam(value = "capacidadVolumen") BigDecimal volumen
    );

    @PostMapping("/register")
    ContenedorRegisterResponse register(@RequestBody ContenedorRegisterRequest request);

    @PutMapping("/asignarCliente")
    ResponseEntity<Void> asignarCliente(@RequestBody ContenedorAsignarClienteRequest request);

}

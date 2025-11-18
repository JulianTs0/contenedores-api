package backend.grupo130.envios.client.contenedores;

import backend.grupo130.envios.client.contenedores.request.ContenedorAsignarClienteRequest;
import backend.grupo130.envios.client.contenedores.request.ContenedorRegisterRequest;
import backend.grupo130.envios.client.contenedores.request.GetByPesoVolumenRequest;
import backend.grupo130.envios.client.contenedores.responses.ContenedorGetByIdResponse;
import backend.grupo130.envios.client.contenedores.responses.ContenedorGetByPesoVolumenResponse;
import backend.grupo130.envios.client.contenedores.responses.RegisterResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "contenedores", url = "${spring.clients.contenedores.url}")
public interface ContenedorClient {

    @GetMapping("/getById/{id}")
    ContenedorGetByIdResponse getBYId(@PathVariable("id") Long id);

    @PostMapping("/getByPesoVolumen")
    ContenedorGetByPesoVolumenResponse getByPesoVolumen(@RequestBody GetByPesoVolumenRequest request);

    @PostMapping("/register")
    RegisterResponse register(@RequestBody ContenedorRegisterRequest request);

    @PostMapping("/asignarCliente")
    ResponseEntity<Void> asignarCliente(@RequestBody ContenedorAsignarClienteRequest request);

}

package backend.grupo130.tramos.client.contenedores;

import backend.grupo130.tramos.client.contenedores.request.ContenedorCambioDeEstadoRequest;
import backend.grupo130.tramos.client.contenedores.responses.ContenedorCambioDeEstadoResponse;
import backend.grupo130.tramos.client.contenedores.responses.ContenedorGetByIdResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "contenedores", url = "${spring.clients.contenedores.url}")
public interface ContenedorClient {

    @GetMapping("/getById/{id}")
    ContenedorGetByIdResponse getBYId(@PathVariable("id") Long id);

    @PutMapping("/cambioDeEstado")
    ContenedorCambioDeEstadoResponse cambioDeEstado(@RequestBody ContenedorCambioDeEstadoRequest request);

}

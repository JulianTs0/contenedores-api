package backend.grupo130.tramos.client.contenedores;

import backend.grupo130.tramos.client.contenedores.responses.ContenedorCambioDeEstadoResponse;
import backend.grupo130.tramos.client.contenedores.responses.ContenedorGetByIdResponse;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(name = "contenedores", url = "${spring.clients.contenedores.url}")
public interface ContenedorGateway {

    @GetMapping("/{id}")
    ContenedorGetByIdResponse getById(
        @PathVariable("id") Long id
    );

    @PutMapping("/estado/{id}")
    ContenedorCambioDeEstadoResponse cambioDeEstado(
        @PathVariable("id") Long id,
        @RequestBody @Valid Map<String, Object> body
    );

}

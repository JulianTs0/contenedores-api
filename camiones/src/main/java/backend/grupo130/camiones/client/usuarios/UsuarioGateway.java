package backend.grupo130.camiones.client.usuarios;

import backend.grupo130.camiones.client.usuarios.responses.UsuarioGetByIdResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "usuarios", url = "${spring.clients.usuarios.url}")
public interface UsuarioGateway {

    @GetMapping("/{id}")
    UsuarioGetByIdResponse getById(@PathVariable("id") Long id);

}

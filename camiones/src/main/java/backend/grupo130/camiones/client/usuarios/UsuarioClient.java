package backend.grupo130.camiones.client.usuarios;

import backend.grupo130.camiones.client.usuarios.responses.GetUserByIdResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "usuarios", url = "${spring.clients.usuarios.url}")
public interface UsuarioClient {

    @GetMapping("/getById/{id}")
    GetUserByIdResponse getBYId(@PathVariable("id") Long id);

}

package backend.grupo130.camiones.client.usuarios;

import backend.grupo130.camiones.client.usuarios.entity.Usuario;
import backend.grupo130.camiones.client.usuarios.responses.UsuarioGetByIdResponse;
import backend.grupo130.camiones.config.enums.Rol;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.Objects;
import java.util.stream.Collectors;

import static net.logstash.logback.argument.StructuredArguments.kv;

@Component
@AllArgsConstructor
@Slf4j
public class UsuarioClient {

    private final UsuarioGateway usuarioGateway;

    public Usuario getById(Long usuarioId){

        log.info("Iniciando búsqueda de usuario por ID", 
            kv("evento", "busqueda_usuario_client"), 
            kv("usuario_id", usuarioId)
        );

        UsuarioGetByIdResponse response = this.usuarioGateway.getById(usuarioId);

        Usuario usuario = Usuario.builder()
            .idUsuario(response.getId())
            .nombre(response.getNombre())
            .apellido(response.getApellido())
            .telefono(response.getTelefono())
            .email(response.getEmail())
            .roles(Rol.fromString(response.getRoles().stream().toList()))
            .build();

        log.debug("Usuario recuperado exitosamente", 
            kv("evento", "busqueda_usuario_client_exitosa"), 
            kv("usuario_id", usuario.getIdUsuario())
        );

        return usuario;

    }

}

package backend.grupo130.contenedores.client.usuarios;

import backend.grupo130.contenedores.client.usuarios.entity.Usuario;
import backend.grupo130.contenedores.client.usuarios.responses.UsuarioGetByIdResponse;
import backend.grupo130.contenedores.config.enums.Errores;
import backend.grupo130.contenedores.config.enums.Rol;
import backend.grupo130.contenedores.config.exceptions.ServiceError;
import feign.FeignException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Component;

import static net.logstash.logback.argument.StructuredArguments.kv;

@Component
@AllArgsConstructor
@Slf4j
public class UsuarioClient {

    private final UsuarioGateway usuarioGateway;

    public Usuario getById(Long usuarioId){
        log.info("Iniciando llamada a microservicio de usuarios", 
            kv("evento", "llamada_ms_usuarios_inicio"), 
            kv("usuario_id", usuarioId)
        );

        UsuarioGetByIdResponse response;
        try {
            response = this.usuarioGateway.getById(usuarioId);
        } catch (FeignException e) {
            log.error("Error al llamar al microservicio de usuarios", 
                kv("evento", "llamada_ms_usuarios_error"), 
                kv("usuario_id", usuarioId), 
                kv("error", e.getMessage())
            );
            throw e;
        }

        Usuario usuario = Usuario.builder()
            .idUsuario(response.getId())
            .nombre(response.getNombre())
            .apellido(response.getApellido())
            .telefono(response.getTelefono())
            .email(response.getEmail())
            .roles(Rol.fromString(response.getRoles().stream().toList()))
            .build();

        log.info("Llamada a microservicio de usuarios exitosa", 
            kv("evento", "llamada_ms_usuarios_exito"), 
            kv("usuario_id", usuarioId)
        );
        return usuario;

    }

}

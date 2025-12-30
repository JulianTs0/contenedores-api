package backend.grupo130.contenedores.client.usuarios;

import backend.grupo130.contenedores.client.usuarios.entity.Usuario;
import backend.grupo130.contenedores.client.usuarios.responses.UsuarioGetByIdResponse;
import backend.grupo130.contenedores.config.enums.Errores;
import backend.grupo130.contenedores.config.enums.Rol;
import backend.grupo130.contenedores.config.exceptions.ServiceError;
import feign.FeignException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UsuarioClient {

    private final UsuarioGateway usuarioGateway;

    public Usuario getById(Long usuarioId){

        UsuarioGetByIdResponse response = this.usuarioGateway.getById(usuarioId);

        Usuario usuario = Usuario.builder()
            .idUsuario(response.getId())
            .nombre(response.getNombre())
            .apellido(response.getApellido())
            .telefono(response.getTelefono())
            .email(response.getEmail())
            .roles(Rol.fromString(response.getRoles().stream().toList()))
            .build();

        return usuario;

    }

}

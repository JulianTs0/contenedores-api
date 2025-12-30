package backend.grupo130.camiones.client.usuarios;

import backend.grupo130.camiones.client.usuarios.entity.Usuario;
import backend.grupo130.camiones.client.usuarios.responses.UsuarioGetByIdResponse;
import backend.grupo130.camiones.config.enums.Rol;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Objects;
import java.util.stream.Collectors;

@Repository
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

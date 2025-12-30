package backend.grupo130.contenedores.client.usuarios.responses;

import backend.grupo130.contenedores.config.enums.Rol;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;

@Getter
@AllArgsConstructor
public class UsuarioGetByIdResponse {

    private final Long id;

    private final String nombre;

    private final String apellido;

    private final String telefono;

    private final String email;

    private final Set<String> roles;

}

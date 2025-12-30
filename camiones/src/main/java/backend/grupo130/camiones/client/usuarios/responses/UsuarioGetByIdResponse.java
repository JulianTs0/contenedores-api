package backend.grupo130.camiones.client.usuarios.responses;

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

    private Set<String> roles;

}

package backend.grupo130.usuarios.dto.keylock.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class KeylockCrearUsuarioRequest {

    private final String email;

    private final String nombre;

    private final String apellido;

    private final String password;

}

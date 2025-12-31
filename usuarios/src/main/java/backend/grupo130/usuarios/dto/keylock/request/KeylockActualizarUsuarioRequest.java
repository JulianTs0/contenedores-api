package backend.grupo130.usuarios.dto.keylock.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class KeylockActualizarUsuarioRequest {

    private final String keycloakId;

    private final String nuevoNombre;

    private final String nuevoApellido;

    private final String nuevoEmail;

}

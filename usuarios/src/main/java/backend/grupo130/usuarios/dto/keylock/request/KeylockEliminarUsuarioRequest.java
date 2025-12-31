package backend.grupo130.usuarios.dto.keylock.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class KeylockEliminarUsuarioRequest {

    private final String keycloakId;

}

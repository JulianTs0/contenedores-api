package backend.grupo130.usuarios.dto.keylock.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class KeylockAsignarRolRequest {

    private final String keycloakId;

    private final List<String> roles;

}

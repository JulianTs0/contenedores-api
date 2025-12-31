package backend.grupo130.usuarios.dto.keylock;

import backend.grupo130.usuarios.dto.keylock.request.KeylockActualizarUsuarioRequest;
import backend.grupo130.usuarios.dto.keylock.request.KeylockAsignarRolRequest;
import backend.grupo130.usuarios.dto.keylock.request.KeylockCrearUsuarioRequest;
import backend.grupo130.usuarios.dto.keylock.request.KeylockEliminarUsuarioRequest;
import backend.grupo130.usuarios.dto.keylock.response.KeylockCrearUsuarioResponse;

import java.util.List;

public class KeylockMapperDto {

    public static KeylockCrearUsuarioRequest toRequestCrearUsuario(String email, String nombre, String apellido, String password) {
        return KeylockCrearUsuarioRequest.builder()
            .email(email)
            .nombre(nombre)
            .apellido(apellido)
            .password(password)
            .build();
    }

    public static KeylockActualizarUsuarioRequest toRequestActualizarUsuario(String keycloakId, String nuevoNombre, String nuevoApellido, String nuevoEmail) {
        return KeylockActualizarUsuarioRequest.builder()
            .keycloakId(keycloakId)
            .nuevoNombre(nuevoNombre)
            .nuevoApellido(nuevoApellido)
            .nuevoEmail(nuevoEmail)
            .build();
    }

    public static KeylockEliminarUsuarioRequest toRequestEliminarUsuario(String keycloakId) {
        return KeylockEliminarUsuarioRequest.builder()
            .keycloakId(keycloakId)
            .build();
    }

    public static KeylockAsignarRolRequest toRequestAsignarRol(String keycloakId, List<String> roles) {
        return KeylockAsignarRolRequest.builder()
            .keycloakId(keycloakId)
            .roles(roles)
            .build();
    }

    public static KeylockCrearUsuarioResponse toResponseCrearUsuario(String userId) {
        return new KeylockCrearUsuarioResponse(userId);
    }

}

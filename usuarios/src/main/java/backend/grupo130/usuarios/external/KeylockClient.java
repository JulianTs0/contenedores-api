package backend.grupo130.usuarios.external;

import backend.grupo130.usuarios.config.enums.Errores;
import backend.grupo130.usuarios.config.exceptions.ServiceError;
import backend.grupo130.usuarios.dto.keylock.KeylockMapperDto;
import backend.grupo130.usuarios.dto.keylock.request.KeylockActualizarUsuarioRequest;
import backend.grupo130.usuarios.dto.keylock.request.KeylockAsignarRolRequest;
import backend.grupo130.usuarios.dto.keylock.request.KeylockCrearUsuarioRequest;
import backend.grupo130.usuarios.dto.keylock.request.KeylockEliminarUsuarioRequest;
import backend.grupo130.usuarios.dto.keylock.response.KeylockCrearUsuarioResponse;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class KeylockClient {

    private final Keycloak keycloak;

    @Value("${keycloak.realm}")
    private String realm;

    public KeylockCrearUsuarioResponse crearUsuarioKeylock(KeylockCrearUsuarioRequest request) {
        log.info("Iniciando creación de usuario en Keycloak para email: {}", request.getEmail());

        UserRepresentation user = new UserRepresentation();
        user.setEnabled(true);
        user.setUsername(request.getEmail());
        user.setEmail(request.getEmail());
        user.setFirstName(request.getNombre());
        user.setLastName(request.getApellido());
        user.setEmailVerified(true);

        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(request.getPassword());
        credential.setTemporary(false);
        user.setCredentials(Collections.singletonList(credential));

        UsersResource usersResource = this.keycloak.realm(this.realm).users();
        Response response = usersResource.create(user);

        String errorBody = response.readEntity(String.class); // <--- ESTO ES CLAVE

        log.error("Error creando usuario en Keycloak. Status: {}. Mensaje: {}", response.getStatus(), errorBody);

        if (response.getStatus() == 201) {
            String path = response.getLocation().getPath();
            String userId = path.substring(path.lastIndexOf("/") + 1);

            response.close();
            log.info("Usuario creado exitosamente en Keycloak con ID: {}", userId);
            return KeylockMapperDto.toResponseCrearUsuario(userId);
        } else {
            log.error("Error al crear usuario en Keycloak. Status: {}", response.getStatus());
            response.close();
            throw new ServiceError("", Errores.ERROR_INTERNO, 500);
        }
    }

    public void actualizarUsuarioKeylock(KeylockActualizarUsuarioRequest request) {
        log.info("Iniciando actualización de usuario en Keycloak con ID: {}", request.getKeycloakId());
        UserResource userResource = this.keycloak.realm(realm).users().get(request.getKeycloakId());

        UserRepresentation user = userResource.toRepresentation();

        if (request.getNuevoNombre() != null)
            user.setFirstName(request.getNuevoNombre());
        if (request.getNuevoApellido() != null)
            user.setLastName(request.getNuevoApellido());
        if (request.getNuevoEmail() != null) {
            user.setEmail(request.getNuevoEmail());
            user.setUsername(request.getNuevoEmail());
        }

        userResource.update(user);
        log.info("Usuario actualizado exitosamente en Keycloak con ID: {}", request.getKeycloakId());
    }

    public void eliminarKeycloakUser(KeylockEliminarUsuarioRequest request) {
        log.info("Iniciando eliminación de usuario en Keycloak con ID: {}", request.getKeycloakId());
        this.keycloak.realm(realm).users().get(request.getKeycloakId()).remove();
        log.info("Usuario eliminado exitosamente en Keycloak con ID: {}", request.getKeycloakId());
    }

    public void asignarRoles(KeylockAsignarRolRequest request) {
        if (request.getRoles() == null || request.getRoles().isEmpty()) {
            log.warn("Intento de asignar roles vacío para usuario ID: {}", request.getKeycloakId());
            return;
        }

        log.info("Asignando roles {} al usuario Keycloak ID: {}", request.getRoles(), request.getKeycloakId());
        UserResource userResource = keycloak.realm(realm).users().get(request.getKeycloakId());
        List<RoleRepresentation> rolesToAdd = new ArrayList<>();

        for (String rolName : request.getRoles()) {
            try {
                RoleRepresentation role = keycloak.realm(realm).roles().get(rolName).toRepresentation();
                rolesToAdd.add(role);
            } catch (Exception e) {
                log.error("Error al obtener rol '{}' de Keycloak: {}", rolName, e.getMessage());
            }
        }

        if (!rolesToAdd.isEmpty()) {
            userResource.roles().realmLevel().add(rolesToAdd);
            log.info("Roles asignados exitosamente al usuario ID: {}", request.getKeycloakId());
        } else {
            log.warn("No se pudieron asignar roles al usuario ID: {} (posiblemente roles no encontrados)", request.getKeycloakId());
        }
    }

    public void actualizarRoles(KeylockAsignarRolRequest request) {
        log.info("Actualizando roles para usuario Keycloak ID: {}", request.getKeycloakId());
        UserResource userResource = keycloak.realm(realm).users().get(request.getKeycloakId());

        List<RoleRepresentation> currentRoles = userResource.roles().realmLevel().listAll();

        if (!currentRoles.isEmpty()) {
            log.debug("Eliminando roles actuales para usuario ID: {}", request.getKeycloakId());
            userResource.roles().realmLevel().remove(currentRoles);
        }

        this.asignarRoles(request);
        log.info("Roles actualizados exitosamente para usuario ID: {}", request.getKeycloakId());
    }

}

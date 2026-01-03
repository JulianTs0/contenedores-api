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

import static net.logstash.logback.argument.StructuredArguments.kv;

@Component
@RequiredArgsConstructor
@Slf4j
public class KeylockClient {

    private final Keycloak keycloak;

    @Value("${keycloak.realm}")
    private String realm;

    public KeylockCrearUsuarioResponse crearUsuarioKeylock(KeylockCrearUsuarioRequest request) {
        log.info("Iniciando creación de usuario en Keycloak", 
            kv("evento", "crear_usuario_keycloak"), 
            kv("email", request.getEmail()));

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

        if (response.getStatus() != 201) {
            log.error("Error creando usuario en Keycloak", 
                kv("evento", "error_crear_usuario_keycloak"), 
                kv("status", response.getStatus()), 
                kv("mensaje", errorBody));
        }

        if (response.getStatus() == 201) {
            String path = response.getLocation().getPath();
            String userId = path.substring(path.lastIndexOf("/") + 1);

            response.close();
            log.info("Usuario creado exitosamente en Keycloak", 
                kv("evento", "usuario_creado_keycloak"), 
                kv("keycloak_id", userId));
            return KeylockMapperDto.toResponseCrearUsuario(userId);
        } else {
            log.error("Error al crear usuario en Keycloak", 
                kv("evento", "error_crear_usuario_keycloak"), 
                kv("status", response.getStatus()));
            response.close();
            throw new ServiceError("", Errores.ERROR_INTERNO, 500);
        }
    }

    public void actualizarUsuarioKeylock(KeylockActualizarUsuarioRequest request) {
        log.info("Iniciando actualización de usuario en Keycloak", 
            kv("evento", "actualizar_usuario_keycloak"), 
            kv("keycloak_id", request.getKeycloakId()));
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
        log.info("Usuario actualizado exitosamente en Keycloak", 
            kv("evento", "usuario_actualizado_keycloak"), 
            kv("keycloak_id", request.getKeycloakId()));
    }

    public void eliminarKeycloakUser(KeylockEliminarUsuarioRequest request) {
        log.info("Iniciando eliminación de usuario en Keycloak", 
            kv("evento", "eliminar_usuario_keycloak"), 
            kv("keycloak_id", request.getKeycloakId()));
        this.keycloak.realm(realm).users().get(request.getKeycloakId()).remove();
        log.info("Usuario eliminado exitosamente en Keycloak", 
            kv("evento", "usuario_eliminado_keycloak"), 
            kv("keycloak_id", request.getKeycloakId()));
    }

    public void asignarRoles(KeylockAsignarRolRequest request) {
        if (request.getRoles() == null || request.getRoles().isEmpty()) {
            log.warn("Intento de asignar roles vacío", 
                kv("evento", "asignar_roles_vacio"), 
                kv("keycloak_id", request.getKeycloakId()));
            return;
        }

        log.info("Asignando roles al usuario Keycloak", 
            kv("evento", "asignar_roles_keycloak"), 
            kv("roles", request.getRoles()), 
            kv("keycloak_id", request.getKeycloakId()));
        UserResource userResource = keycloak.realm(realm).users().get(request.getKeycloakId());
        List<RoleRepresentation> rolesToAdd = new ArrayList<>();

        for (String rolName : request.getRoles()) {
            try {
                RoleRepresentation role = keycloak.realm(realm).roles().get(rolName).toRepresentation();
                rolesToAdd.add(role);
            } catch (Exception e) {
                log.error("Error al obtener rol de Keycloak", 
                    kv("evento", "error_obtener_rol_keycloak"), 
                    kv("rol", rolName), 
                    kv("error", e.getMessage()));
            }
        }

        if (!rolesToAdd.isEmpty()) {
            userResource.roles().realmLevel().add(rolesToAdd);
            log.info("Roles asignados exitosamente al usuario", 
                kv("evento", "roles_asignados_keycloak"), 
                kv("keycloak_id", request.getKeycloakId()));
        } else {
            log.warn("No se pudieron asignar roles al usuario (posiblemente roles no encontrados)", 
                kv("evento", "error_asignar_roles_keycloak"), 
                kv("keycloak_id", request.getKeycloakId()));
        }
    }

    public void actualizarRoles(KeylockAsignarRolRequest request) {
        log.info("Actualizando roles para usuario Keycloak", 
            kv("evento", "actualizar_roles_keycloak"), 
            kv("keycloak_id", request.getKeycloakId()));
        UserResource userResource = keycloak.realm(realm).users().get(request.getKeycloakId());

        List<RoleRepresentation> currentRoles = userResource.roles().realmLevel().listAll();

        if (!currentRoles.isEmpty()) {
            log.debug("Eliminando roles actuales para usuario", 
                kv("evento", "eliminar_roles_actuales_keycloak"), 
                kv("keycloak_id", request.getKeycloakId()));
            userResource.roles().realmLevel().remove(currentRoles);
        }

        this.asignarRoles(request);
        log.info("Roles actualizados exitosamente para usuario", 
            kv("evento", "roles_actualizados_keycloak"), 
            kv("keycloak_id", request.getKeycloakId()));
    }

}

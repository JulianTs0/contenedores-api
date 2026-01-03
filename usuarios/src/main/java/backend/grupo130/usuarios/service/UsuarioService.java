package backend.grupo130.usuarios.service;

import backend.grupo130.usuarios.config.enums.Errores;
import backend.grupo130.usuarios.config.enums.Rol;
import backend.grupo130.usuarios.config.exceptions.ServiceError;
import backend.grupo130.usuarios.data.entity.Usuario;
import backend.grupo130.usuarios.dto.keylock.KeylockMapperDto;
import backend.grupo130.usuarios.dto.usuarios.UsuarioMapperDto;
import backend.grupo130.usuarios.dto.usuarios.request.DeleteRequest;
import backend.grupo130.usuarios.dto.usuarios.request.EditRequest;
import backend.grupo130.usuarios.dto.usuarios.request.GetByIdRequest;
import backend.grupo130.usuarios.dto.usuarios.request.RegisterRequest;
import backend.grupo130.usuarios.dto.usuarios.response.EditResponse;
import backend.grupo130.usuarios.dto.usuarios.response.GetAllResponse;
import backend.grupo130.usuarios.dto.usuarios.response.GetByIdResponse;
import backend.grupo130.usuarios.dto.usuarios.response.RegisterResponse;
import backend.grupo130.usuarios.external.KeylockClient;
import backend.grupo130.usuarios.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static net.logstash.logback.argument.StructuredArguments.kv;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UsuarioService {

    // TODO: Implementar el rollback de keylock

    private final UsuarioRepository usuarioRepository;

    private final KeylockClient keylockClient;

    // GET

    public GetByIdResponse getById(GetByIdRequest request) throws ServiceError {
        log.info("Iniciando búsqueda de usuarioModel", 
            kv("evento", "obtener_usuario_por_id"), 
            kv("usuario_id", request.getUsuarioId()));

        Usuario usuario = this.usuarioRepository.getById(request.getUsuarioId());

        if (usuario == null) {
            log.warn("No se encontró usuarioModel", 
                kv("evento", "usuario_no_encontrado"), 
                kv("usuario_id", request.getUsuarioId()));
            throw new ServiceError("", Errores.USUARIO_NO_ENCONTRADO, 404);
        }

        log.info("UsuarioModel encontrado exitosamente", 
            kv("evento", "usuario_encontrado"), 
            kv("usuario_id", request.getUsuarioId()));
        GetByIdResponse response = UsuarioMapperDto.toResponseGetId(usuario);

        return response;

    }

    public GetAllResponse getAll() throws ServiceError {
        log.info("Iniciando obtención de todos los usuarioModels", 
            kv("evento", "obtener_todos_usuarios"));

        List<Usuario> usuarios = this.usuarioRepository.getAll();

        log.info("Se obtuvieron usuarioModels exitosamente", 
            kv("evento", "usuarios_obtenidos"), 
            kv("cantidad", usuarios.size()));
        GetAllResponse response = UsuarioMapperDto.toResponseGetAll(usuarios);

        return response;
    }

    // POST

    public RegisterResponse register(RegisterRequest request) throws ServiceError {
        log.info("Iniciando registro de nuevo usuarioModel", 
            kv("evento", "registrar_usuario"), 
            kv("email", request.getEmail()));

        String keyclockId = this.keylockClient.crearUsuarioKeylock(
            KeylockMapperDto.toRequestCrearUsuario(
                request.getEmail(),
                request.getNombre(),
                request.getApellido(),
                request.getPassword()
            )
        ).getUserId();

        this.keylockClient.asignarRoles(
            KeylockMapperDto.toRequestAsignarRol(keyclockId, request.getRoles())
        );

        Usuario usuario = new Usuario();

        usuario.setKeycloakId(keyclockId);
        usuario.setNombre(request.getNombre());
        usuario.setApellido(request.getApellido());
        usuario.setTelefono(request.getTelefono());
        usuario.setEmail(request.getEmail());

        Set<Rol> roles = Rol.fromString(request.getRoles());

        if (roles.isEmpty()){
            log.warn("Intento de registro con rol inválido", 
                kv("evento", "rol_invalido"), 
                kv("roles", request.getRoles()));
            this.keylockClient.eliminarKeycloakUser(
                KeylockMapperDto.toRequestEliminarUsuario(keyclockId)
            );
            throw new ServiceError("", Errores.ROL_INVALIDO, 400);
        }

        usuario.setRoles(roles);

        Usuario saved = this.usuarioRepository.save(usuario);
        log.info("UsuarioModel registrado exitosamente", 
            kv("evento", "usuario_registrado"), 
            kv("email", saved.getEmail()));

        return UsuarioMapperDto.toResponsePostRegister(saved);
    }

    // PATCH

    public EditResponse edit(EditRequest request) throws ServiceError {
        log.info("Iniciando edición de usuarioModel", 
            kv("evento", "editar_usuario"), 
            kv("usuario_id", request.getId()));

        Usuario usuario = this.usuarioRepository.getById(request.getId());

        if (usuario == null) {
            log.warn("Intento de edición fallido. No se encontró usuarioModel", 
                kv("evento", "edicion_fallida_usuario_no_encontrado"), 
                kv("usuario_id", request.getId()));
            throw new ServiceError("", Errores.USUARIO_NO_ENCONTRADO, 404);
        }

        this.keylockClient.actualizarUsuarioKeylock(
            KeylockMapperDto.toRequestActualizarUsuario(
                usuario.getKeycloakId(),
                request.getNombre(),
                request.getApellido(),
                request.getEmail()
            )
        );

        log.debug("UsuarioModel encontrado. Aplicando actualizaciones...", 
            kv("evento", "aplicando_actualizaciones"), 
            kv("usuario_id", usuario.getIdUsuario()));

        if (request.getRoles() != null && !request.getRoles().isEmpty()) {

            Set<Rol> nuevosRolesEnum = Rol.fromString(request.getRoles());

            List<String> nuevosRolesString = nuevosRolesEnum.stream()
                .map(Rol::name)
                .collect(Collectors.toList());

            this.keylockClient.actualizarRoles(
                KeylockMapperDto.toRequestAsignarRol(usuario.getKeycloakId(), nuevosRolesString)
            );

            usuario.setRoles(nuevosRolesEnum);
        }
        if (request.getNombre() != null) {
            usuario.setNombre(request.getNombre());
        }
        if (request.getApellido() != null) {
            usuario.setApellido(request.getApellido());
        }
        if (request.getTelefono() != null) {
            usuario.setTelefono(request.getTelefono());
        }
        if (request.getEmail() != null) {
            usuario.setEmail(request.getEmail());
        }

        Usuario updated = this.usuarioRepository.update(usuario);
        log.info("UsuarioModel editado exitosamente", 
            kv("evento", "usuario_editado"), 
            kv("usuario_id", updated.getIdUsuario()));

        EditResponse response = UsuarioMapperDto.toResponsePatchEdit(updated);

        return response;

    }

    public void delete(DeleteRequest request) throws ServiceError {
        log.info("Iniciando eliminación de usuario", 
            kv("evento", "eliminar_usuario"), 
            kv("usuario_id", request.getId()));

        Usuario usuario = this.usuarioRepository.getById(request.getId());

        if (usuario == null) {
            throw new ServiceError("", Errores.USUARIO_NO_ENCONTRADO, 404);
        }

        this.keylockClient.eliminarKeycloakUser(
            KeylockMapperDto.toRequestEliminarUsuario(usuario.getKeycloakId())
        );
        log.info("Usuario eliminado exitosamente", 
            kv("evento", "usuario_eliminado"), 
            kv("usuario_id", request.getId()));

        this.usuarioRepository.delete(usuario.getIdUsuario());

    }

}

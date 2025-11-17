package backend.grupo130.usuarios.service;

import backend.grupo130.usuarios.config.enums.Errores;
import backend.grupo130.usuarios.config.enums.Rol;
import backend.grupo130.usuarios.config.exceptions.ServiceError;
import backend.grupo130.usuarios.data.models.Usuario;
import backend.grupo130.usuarios.dto.UsuarioMapperDto;
import backend.grupo130.usuarios.dto.request.EditRequest;
import backend.grupo130.usuarios.dto.request.GetByIdRequest;
import backend.grupo130.usuarios.dto.request.RegisterRequest;
import backend.grupo130.usuarios.dto.response.EditResponse;
import backend.grupo130.usuarios.dto.response.GetAllResponse;
import backend.grupo130.usuarios.dto.response.GetByIdResponse;
import backend.grupo130.usuarios.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    // GET

    public GetByIdResponse getById(GetByIdRequest request) throws ServiceError {
        log.info("Iniciando búsqueda de usuario por ID: {}", request.getUsuarioId());
        try {

            Usuario usuario = this.usuarioRepository.getById(request.getUsuarioId());

            if (usuario == null) {
                log.warn("No se encontró usuario con ID: {}", request.getUsuarioId());
                throw new ServiceError("", Errores.USUARIO_NO_ENCONTRADO, 404);
            }

            log.info("Usuario encontrado exitosamente con ID: {}", request.getUsuarioId());
            GetByIdResponse response = UsuarioMapperDto.toResponseGet(usuario);

            return response;
        } catch (ServiceError ex) {
            throw ex; // Se relanza la excepción de negocio (ya logueada donde se originó)
        }
        catch (Exception ex) {
            // Error inesperado: Se loguea con nivel ERROR y el stack trace
            log.error("Error interno al buscar usuario por ID: {}", request.getUsuarioId(), ex);
            throw new ServiceError(ex.getMessage(), Errores.ERROR_INTERNO , 500);
        }
    }

    public GetAllResponse getAll() throws ServiceError {
        log.info("Iniciando obtención de todos los usuarios");
        try {

            List<Usuario> usuarios = this.usuarioRepository.getAll();

            log.info("Se obtuvieron {} usuarios exitosamente", usuarios.size());
            GetAllResponse response = UsuarioMapperDto.toResponseGet(usuarios);

            return response;
        } catch (ServiceError ex) {
            throw ex;
        }  catch (Exception ex) {
            log.error("Error interno al obtener todos los usuarios", ex);
            throw new ServiceError(ex.getMessage(), Errores.ERROR_INTERNO , 500);
        }
    }

    // POST

    public void register(RegisterRequest request) throws ServiceError {
        log.info("Iniciando registro de nuevo usuario con email: {}", request.getEmail());
        try {

            Usuario usuario = new Usuario();

            usuario.setNombre(request.getNombre());
            usuario.setApellido(request.getApellido());
            usuario.setTelefono(request.getTelefono());
            usuario.setEmail(request.getEmail());

            Rol rol = Rol.fromString(request.getRol());

            if (rol == null){
                log.warn("Intento de registro con rol inválido: {}", request.getRol());
                throw new ServiceError("", Errores.ROL_INVALIDO, 400);
            }

            usuario.setRol(rol);

            this.usuarioRepository.save(usuario);
            log.info("Usuario registrado exitosamente con email: {}", usuario.getEmail());

        } catch (ServiceError ex) {
            throw ex; // Relanzamos la excepción de negocio (rol inválido)
        } catch (Exception ex) {
            log.error("Error interno al registrar usuario con email: {}", request.getEmail(), ex);
            throw new ServiceError(ex.getMessage(), Errores.ERROR_INTERNO , 500);
        }
    }

    // PATCH

    public EditResponse edit(EditRequest request) throws ServiceError {
        log.info("Iniciando edición de usuario con ID: {}", request.getId());
        try {

            Usuario usuario = this.usuarioRepository.getById(request.getId());

            if (usuario == null) {
                log.warn("Intento de edición fallido. No se encontró usuario con ID: {}", request.getId());
                throw new ServiceError("", Errores.USUARIO_NO_ENCONTRADO, 404);
            }

            log.debug("Usuario ID: {} encontrado. Aplicando actualizaciones...", usuario.getIdUsuario());

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

            this.usuarioRepository.update(usuario);
            log.info("Usuario con ID: {} editado exitosamente", usuario.getIdUsuario());

            EditResponse response = UsuarioMapperDto.toResponsePatch(usuario);

            return response;
        } catch (ServiceError ex) {
            throw ex; // Relanzamos la excepción de negocio (no encontrado)
        } catch (Exception ex) {
            log.error("Error interno al editar usuario con ID: {}", request.getId(), ex);
            throw new ServiceError(ex.getMessage(), Errores.ERROR_INTERNO , 500);
        }
    }

}

package backend.grupo130.usuarios.service;

import backend.grupo130.usuarios.config.enums.Errores;
import backend.grupo130.usuarios.config.enums.Rol;
import backend.grupo130.usuarios.config.exceptions.ServiceError;
import backend.grupo130.usuarios.data.entity.Usuario;
import backend.grupo130.usuarios.dto.UsuarioMapperDto;
import backend.grupo130.usuarios.dto.request.EditRequest;
import backend.grupo130.usuarios.dto.request.GetByIdRequest;
import backend.grupo130.usuarios.dto.request.RegisterRequest;
import backend.grupo130.usuarios.dto.response.EditResponse;
import backend.grupo130.usuarios.dto.response.GetAllResponse;
import backend.grupo130.usuarios.dto.response.GetByIdResponse;
import backend.grupo130.usuarios.dto.response.RegisterResponse;
import backend.grupo130.usuarios.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
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
        log.info("Iniciando búsqueda de usuarioModel por ID: {}", request.getUsuarioId());

        Usuario usuario = this.usuarioRepository.getById(request.getUsuarioId());

        if (usuario == null) {
            log.warn("No se encontró usuarioModel con ID: {}", request.getUsuarioId());
            throw new ServiceError("", Errores.USUARIO_NO_ENCONTRADO, 404);
        }

        log.info("UsuarioModel encontrado exitosamente con ID: {}", request.getUsuarioId());
        GetByIdResponse response = UsuarioMapperDto.toResponseGetId(usuario);

        return response;

    }

    public GetAllResponse getAll() throws ServiceError {
        log.info("Iniciando obtención de todos los usuarioModels");

        List<Usuario> usuarios = this.usuarioRepository.getAll();

        log.info("Se obtuvieron {} usuarioModels exitosamente", usuarios.size());
        GetAllResponse response = UsuarioMapperDto.toResponseGetAll(usuarios);

        return response;
    }

    // POST

    public RegisterResponse register(RegisterRequest request) throws ServiceError {
        log.info("Iniciando registro de nuevo usuarioModel con email: {}", request.getEmail());

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

        Usuario saved = this.usuarioRepository.save(usuario);
        log.info("UsuarioModel registrado exitosamente con email: {}", saved.getEmail());

        return UsuarioMapperDto.toResponsePostRegister(saved);
    }

    // PATCH

    public EditResponse edit(EditRequest request) throws ServiceError {
        log.info("Iniciando edición de usuarioModel con ID: {}", request.getId());

        Usuario usuario = this.usuarioRepository.getById(request.getId());

        if (usuario == null) {
            log.warn("Intento de edición fallido. No se encontró usuarioModel con ID: {}", request.getId());
            throw new ServiceError("", Errores.USUARIO_NO_ENCONTRADO, 404);
        }

        log.debug("UsuarioModel ID: {} encontrado. Aplicando actualizaciones...", usuario.getIdUsuario());

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
        log.info("UsuarioModel con ID: {} editado exitosamente", updated.getIdUsuario());

        EditResponse response = UsuarioMapperDto.toResponsePatchEdit(updated);

        return response;

    }

}

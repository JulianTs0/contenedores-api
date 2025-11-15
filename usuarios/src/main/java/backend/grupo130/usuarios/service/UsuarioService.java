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
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    // GET

    public GetByIdResponse getById(GetByIdRequest request) throws ServiceError {
        try {

            Usuario usuario = this.usuarioRepository.getById(request.getUsuarioId());

            if (usuario == null) {
                throw new ServiceError("", Errores.USUARIO_NO_ENCONTRADO, 404);
            }

            GetByIdResponse response = UsuarioMapperDto.toResponseGet(usuario);

            return response;
        } catch (ServiceError ex) {
            throw ex;
        }
        catch (Exception ex) {
            throw new ServiceError(ex.getMessage(), Errores.ERROR_INTERNO , 500);
        }
    }

    public GetAllResponse getAll() throws ServiceError {
        try {

            List<Usuario> usuarios = this.usuarioRepository.getAll();

            GetAllResponse response = UsuarioMapperDto.toResponseGet(usuarios);

            return response;
        } catch (ServiceError ex) {
            throw ex;
        }  catch (Exception ex) {
            throw new ServiceError(ex.getMessage(), Errores.ERROR_INTERNO , 500);
        }
    }

    // POST

    public void register(RegisterRequest request) throws ServiceError {
        try {

            Usuario usuario = new Usuario();

            usuario.setNombre(request.getNombre());
            usuario.setApellido(request.getApellido());
            usuario.setTelefono(request.getTelefono());
            usuario.setEmail(request.getEmail());

            Rol rol = Rol.fromString(request.getRol());

            if (rol == null){
                throw new ServiceError("", Errores.ROL_INVALIDO, 400);
            }

            usuario.setRol(rol);

            this.usuarioRepository.save(usuario);
        } catch (ServiceError ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ServiceError(ex.getMessage(), Errores.ERROR_INTERNO , 500);
        }
    }

    // PATCH

    public EditResponse edit(EditRequest request) throws ServiceError {
        try {

            Usuario usuario = this.usuarioRepository.getById(request.getId());

            if (usuario == null) {
                throw new ServiceError("", Errores.USUARIO_NO_ENCONTRADO, 404);
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

            this.usuarioRepository.update(usuario);

            EditResponse response = UsuarioMapperDto.toResponsePatch(usuario);

            return response;
        } catch (ServiceError ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ServiceError(ex.getMessage(), Errores.ERROR_INTERNO , 500);
        }
    }

}

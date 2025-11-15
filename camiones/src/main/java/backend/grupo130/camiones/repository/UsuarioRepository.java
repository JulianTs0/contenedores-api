package backend.grupo130.camiones.repository;

import backend.grupo130.camiones.client.usuarios.UsuarioClient;
import backend.grupo130.camiones.client.usuarios.models.Usuario;
import backend.grupo130.camiones.client.usuarios.responses.UsuarioGetByIdResponse;
import backend.grupo130.camiones.config.enums.Errores;
import backend.grupo130.camiones.config.exceptions.ServiceError;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class UsuarioRepository {

    private final UsuarioClient usuarioRepository;

    public Usuario getById(Long usuarioId){

        try {

            UsuarioGetByIdResponse response = this.usuarioRepository.getBYId(usuarioId);

            Usuario usuario = new Usuario(
                response.getId(),
                response.getNombre(),
                response.getApellido(),
                response.getTelefono(),
                response.getEmail(),
                response.getRol()
            );

            return usuario;

        } catch (ServiceError ex) {
            throw ex;
        } catch (Exception ex){
            throw new ServiceError(ex.getMessage(), Errores.ERROR_INTERNO , 500);
        }

    }

}

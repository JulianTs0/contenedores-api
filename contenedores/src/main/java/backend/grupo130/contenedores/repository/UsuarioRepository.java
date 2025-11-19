package backend.grupo130.contenedores.repository;

import backend.grupo130.contenedores.client.usuarios.UsuarioClient;
import backend.grupo130.contenedores.client.usuarios.entity.Usuario;
import backend.grupo130.contenedores.client.usuarios.responses.UsuarioGetByIdResponse;
import backend.grupo130.contenedores.config.enums.Errores;
import backend.grupo130.contenedores.config.exceptions.ServiceError;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class UsuarioRepository {

    private final UsuarioClient usuarioRepository;

    public Usuario getById(Long usuarioId){

        try {

            UsuarioGetByIdResponse response = this.usuarioRepository.getById(usuarioId);

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

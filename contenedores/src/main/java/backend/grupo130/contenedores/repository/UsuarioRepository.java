package backend.grupo130.contenedores.repository;

import backend.grupo130.contenedores.client.usuarios.UsuarioClient;
import backend.grupo130.contenedores.client.usuarios.models.Usuario;
import backend.grupo130.contenedores.client.usuarios.responses.GetUserByIdResponse;
import backend.grupo130.contenedores.config.exceptions.ServiceError;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@AllArgsConstructor
public class UsuarioRepository {

    private final UsuarioClient usuarioRepository;

    public Usuario getById(Integer usuarioId){

        try {

            GetUserByIdResponse response = this.usuarioRepository.getBYId(usuarioId);

            Usuario usuario = new Usuario(
                usuarioId,
                response.getNombre(),
                response.getApellido(),
                response.getTelefono(),
                response.getEmail(),
                response.getRol()
            );

            return usuario;

        } catch (Exception ex){
            throw new ServiceError("Error interno", 500);
        }

    }

}

package backend.grupo130.camiones.repository;

import backend.grupo130.camiones.client.usuarios.UsuarioClient;
import backend.grupo130.camiones.client.usuarios.models.Usuario;
import backend.grupo130.camiones.client.usuarios.responses.GetUserByIdResponse;
import backend.grupo130.camiones.config.exceptions.ServiceError;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

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

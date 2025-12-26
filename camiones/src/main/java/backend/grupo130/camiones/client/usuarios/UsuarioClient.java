package backend.grupo130.camiones.client.usuarios;

import backend.grupo130.camiones.client.usuarios.entity.Usuario;
import backend.grupo130.camiones.client.usuarios.responses.UsuarioGetByIdResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class UsuarioClient {

    private final UsuarioGateway usuarioGateway;

    public Usuario getById(Long usuarioId){

        UsuarioGetByIdResponse response = this.usuarioGateway.getById(usuarioId);

        Usuario usuario = new Usuario(
            response.getId(),
            response.getNombre(),
            response.getApellido(),
            response.getTelefono(),
            response.getEmail(),
            response.getRol()
        );

        return usuario;

    }

}

package backend.grupo130.camiones.repository;

import backend.grupo130.camiones.client.usuarios.UsuarioClient;
import backend.grupo130.camiones.client.usuarios.entity.Usuario;
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

    }

}

package backend.grupo130.usuarios.service;

import backend.grupo130.usuarios.config.exceptions.ServiceError;
import backend.grupo130.usuarios.data.models.Usuario;
import backend.grupo130.usuarios.dto.request.GetByIdRequest;
import backend.grupo130.usuarios.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Transactional
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public Usuario getById(GetByIdRequest request) throws ServiceError {
        try {

            Usuario usuario = this.usuarioRepository.getById(request.getUsuarioId());

            if (usuario == null) {
                throw new ServiceError("Usuario no encontrado", 404);
            }

            return usuario;
        } catch (Exception ex) {
            throw new ServiceError("Error interno", 500);
        }
    }

}

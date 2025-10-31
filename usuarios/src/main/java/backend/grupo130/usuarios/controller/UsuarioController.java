package backend.grupo130.usuarios.controller;

import backend.grupo130.usuarios.config.exceptions.ServiceError;
import backend.grupo130.usuarios.data.models.Usuario;
import backend.grupo130.usuarios.dto.request.GetByIdRequest;
import backend.grupo130.usuarios.dto.response.GetByIdResponse;
import backend.grupo130.usuarios.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping("/getById/{id}")
    public ResponseEntity<GetByIdResponse> getById(@PathVariable String id) {

        GetByIdRequest request = this.toRequest(id);

        Usuario usuario = this.usuarioService.getById(request);

        return ResponseEntity.ok(this.toResponse(usuario));

    }

    // Respuestas

    private GetByIdResponse toResponse(Usuario usuario) {
        return new GetByIdResponse(
            usuario.getNombre(),
            usuario.getApellido(),
            usuario.getTelefono(),
            usuario.getEmail(),
            usuario.getRol().name()
        );
    }

    // Peticiones

    private GetByIdRequest toRequest(String idParam) {

        try {
            Integer usuarioId = Integer.parseInt(idParam);

            if (usuarioId < 0) {
                throw new ServiceError("La id del usuario es invalida", 400);
            }

            return new GetByIdRequest(
                usuarioId
            );

        } catch (NumberFormatException ex) {
            throw new ServiceError("La id del usuario debe ser un numero", 400);
        }

    }

}

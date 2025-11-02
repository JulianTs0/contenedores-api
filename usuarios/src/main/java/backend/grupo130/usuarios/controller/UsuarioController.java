package backend.grupo130.usuarios.controller;

import backend.grupo130.usuarios.config.exceptions.ServiceError;
import backend.grupo130.usuarios.data.models.Usuario;
import backend.grupo130.usuarios.dto.request.GetByIdRequest;
import backend.grupo130.usuarios.dto.response.GetByIdResponse;
import backend.grupo130.usuarios.service.UsuarioService;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
@Validated
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping("/getById/{id}")
    public ResponseEntity<GetByIdResponse> getById(
        @NotNull(message = "El ID de usuario no puede ser nulo")
        @Min(value = 1, message = "El ID de usuario debe ser un número positivo")
        @PathVariable Integer id
    ) {

        GetByIdRequest request = new GetByIdRequest(id);

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

}

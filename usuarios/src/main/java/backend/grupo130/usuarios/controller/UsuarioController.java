package backend.grupo130.usuarios.controller;

import backend.grupo130.usuarios.config.exceptions.ServiceError;
import backend.grupo130.usuarios.data.models.Usuario;
import backend.grupo130.usuarios.dto.request.EditRequest;
import backend.grupo130.usuarios.dto.request.GetByIdRequest;
import backend.grupo130.usuarios.dto.request.RegisterRequest;
import backend.grupo130.usuarios.dto.response.EditResponse;
import backend.grupo130.usuarios.dto.response.GetAllResponse;
import backend.grupo130.usuarios.dto.response.GetByIdResponse;
import backend.grupo130.usuarios.service.UsuarioService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

        return ResponseEntity.ok(this.toResponseGet(usuario));
    }

    @GetMapping("/getAll")
    public ResponseEntity<?> getAll() {

        List<Usuario> usuarios = this.usuarioService.getAll();

        return ResponseEntity.ok(this.toResponseGet(usuarios));
    }

    @PostMapping("/register")
    public ResponseEntity<GetByIdResponse> register(
        @RequestBody @Valid RegisterRequest request
    ) {

        this.usuarioService.register(request);

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/edit")
    public ResponseEntity<EditResponse> edit(
        @RequestBody @Valid EditRequest request
    ) {

        Usuario usuario = this.usuarioService.edit(request);

        return ResponseEntity.ok(this.toResponsePatch(usuario));
    }

    // Respuestas

    private GetByIdResponse toResponseGet(Usuario usuario) {
        return new GetByIdResponse(
            usuario.getNombre(),
            usuario.getApellido(),
            usuario.getTelefono(),
            usuario.getEmail(),
            usuario.getRol().name()
        );
    }

    private GetAllResponse toResponseGet(List<Usuario> usuarios) {
        return new GetAllResponse(
            usuarios
        );
    }

    private EditResponse toResponsePatch(Usuario usuario) {
        return new EditResponse(
            usuario.getNombre(),
            usuario.getApellido(),
            usuario.getTelefono(),
            usuario.getEmail()
        );
    }

}

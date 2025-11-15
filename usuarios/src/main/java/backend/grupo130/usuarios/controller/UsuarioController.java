package backend.grupo130.usuarios.controller;

import backend.grupo130.usuarios.data.models.Usuario;
import backend.grupo130.usuarios.dto.UsuarioMapperDto;
import backend.grupo130.usuarios.dto.request.EditRequest;
import backend.grupo130.usuarios.dto.request.GetByIdRequest;
import backend.grupo130.usuarios.dto.request.RegisterRequest;
import backend.grupo130.usuarios.dto.response.EditResponse;
import backend.grupo130.usuarios.dto.response.GetByIdResponse;
import backend.grupo130.usuarios.service.UsuarioService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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

    // GET

    @GetMapping("/getById/{id}")
    public ResponseEntity<GetByIdResponse> getById(
        @NotNull(message = "{error.id.notNull}")
        @Positive(message = "{error.id.positive}")
        @PathVariable Long id
    ) {

        GetByIdRequest request = new GetByIdRequest(id);

        return ResponseEntity.ok(this.usuarioService.getById(request));
    }

    @GetMapping("/getAll")
    public ResponseEntity<?> getAll() {

        return ResponseEntity.ok(this.usuarioService.getAll());
    }

    // POST

    @PostMapping("/register")
    public ResponseEntity<GetByIdResponse> register(
        @RequestBody @Valid RegisterRequest request
    ) {

        this.usuarioService.register(request);

        return ResponseEntity.ok().build();
    }

    // PATCH

    @PatchMapping("/edit")
    public ResponseEntity<EditResponse> edit(
        @RequestBody @Valid EditRequest request
    ) {

        return ResponseEntity.ok(this.usuarioService.edit(request));
    }

}

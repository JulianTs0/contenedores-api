package backend.grupo130.usuarios.controller;

import backend.grupo130.usuarios.dto.usuarios.UsuarioMapperDto;
import backend.grupo130.usuarios.dto.usuarios.request.DeleteRequest;
import backend.grupo130.usuarios.dto.usuarios.request.EditRequest;
import backend.grupo130.usuarios.dto.usuarios.request.GetByIdRequest;
import backend.grupo130.usuarios.dto.usuarios.request.RegisterRequest;
import backend.grupo130.usuarios.dto.usuarios.response.EditResponse;
import backend.grupo130.usuarios.dto.usuarios.response.GetAllResponse;
import backend.grupo130.usuarios.dto.usuarios.response.GetByIdResponse;
import backend.grupo130.usuarios.dto.usuarios.response.RegisterResponse;
import backend.grupo130.usuarios.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
@Validated
@Tag(name = "Gestión de Usuarios", description = "API para el alta, edición y consulta de usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    // GET

    @Operation(
        summary = "Obtener un usuario por ID",
        description = "Busca y devuelve los detalles de un usuario específico basado en su ID."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", description = "UsuarioModel encontrado",
            content = {
                @Content(mediaType = "application/json",
                    schema = @Schema(implementation = GetByIdResponse.class))
            }
        ),
        @ApiResponse(
            responseCode = "400",
            description = "ID inválido (no nulo o no positivo)",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "404",
            description = "UsuarioModel no encontrado (lanzado por el servicio)",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Error interno del servidor",
            content = @Content
        )
    })
    @GetMapping("/{id}")
    public ResponseEntity<GetByIdResponse> getById(
        @Parameter(description = "ID del usuario a buscar", required = true, example = "1")
        @NotNull(message = "{error.id.notNull}")
        @Positive(message = "{error.id.positive}")
        @PathVariable Long id
    ) {

        GetByIdRequest request = new GetByIdRequest(id);

        return ResponseEntity.ok(this.usuarioService.getById(request));
    }

    @Operation(
        summary = "Obtener todos los usuarios",
        description = "Devuelve una lista con todos los usuarios registrados en el sistema."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Lista de usuarios obtenida",
            content = {
                @Content(mediaType = "application/json",
                schema = @Schema(implementation = GetAllResponse.class))
            }
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Error interno del servidor",
            content = @Content
        )
    })
    @GetMapping("/")
    public ResponseEntity<GetAllResponse> getAll() {
        return ResponseEntity.ok(this.usuarioService.getAll());
    }

    // POST

    @Operation(
        summary = "Registrar un nuevo usuario",
        description = "Crea un nuevo usuario con la información proporcionada. El email debe ser único."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "UsuarioModel registrado exitosamente",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Datos de registro inválidos (campos faltantes, email incorrecto, rol inválido, etc.)",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Error interno del servidor",
            content = @Content
        )
    })
    @PostMapping("/")
    public ResponseEntity<RegisterResponse> register(
        @RequestBody @Valid RegisterRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.usuarioService.register(request));
    }


    @Operation(
        summary = "Editar un usuario existente",
        description = "Actualiza uno o más campos de un usuario existente. Los campos nulos en el request serán ignorados."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "UsuarioModel editado exitosamente",
            content = {
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = EditResponse.class))
            }
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Datos de edición inválidos (ID nulo, campos con formato incorrecto)",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "404",
            description = "UsuarioModel no encontrado para editar (lanzado por el servicio)",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Error interno del servidor",
            content = @Content
        )
    })
    @PatchMapping("/{id}")
    public ResponseEntity<EditResponse> edit(
        @Parameter(description = "ID del usuario a buscar", required = true, example = "1")
        @NotNull(message = "{error.id.notNull}")
        @Positive(message = "{error.id.positive}")
        @PathVariable Long id,
        @RequestBody @Valid EditRequest body
    ) {
        EditRequest request = UsuarioMapperDto.toRequestPatchEdit(id, body);

        return ResponseEntity.ok(this.usuarioService.edit(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
        @Parameter(description = "ID del usuario a eliminar", required = true, example = "1")
        @NotNull(message = "{error.id.notNull}")
        @Positive(message = "{error.id.positive}")
        @PathVariable Long id
    ) {
        DeleteRequest request = new DeleteRequest(id);
        this.usuarioService.delete(request);
        return ResponseEntity.ok().build();
    }

}

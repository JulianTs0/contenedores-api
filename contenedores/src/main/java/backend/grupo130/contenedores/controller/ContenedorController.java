package backend.grupo130.contenedores.controller;

import backend.grupo130.contenedores.dto.request.*;
import backend.grupo130.contenedores.dto.response.*;
import backend.grupo130.contenedores.service.ContenedorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@Tag(name = "Gestión de Contenedores", description = "API para la creación, consulta, edición y eliminación de contenedores.")
@RestController
@RequestMapping("/api/contenedores")
@RequiredArgsConstructor
@Validated
public class ContenedorController {

    private final ContenedorService contenedorService;

    @Operation(summary = "Obtener un contenedor por ID",
        description = "Busca y devuelve los detalles de un contenedor específico por su ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Contenedor encontrado",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = GetByIdResponse.class))),
        @ApiResponse(responseCode = "400", description = "ID proporcionado inválido (debe ser positivo)",
            content = @Content),
        @ApiResponse(responseCode = "404", description = "Contenedor no encontrado",
            content = @Content),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor",
            content = @Content)
    })
    @GetMapping("/getById/{id}")
    public ResponseEntity<GetByIdResponse> getById(
        @Parameter(description = "ID del contenedor a buscar", required = true, example = "1")
        @NotNull(message = "{error.idContenedor.notNull}")
        @Positive(message = "{error.idContenedor.positive}")
        @PathVariable Long id
    ) {
        GetByIdRequest request = new GetByIdRequest(id);
        return ResponseEntity.ok(this.contenedorService.getById(request));
    }

    @Operation(summary = "Buscar contenedor por peso y volumen",
        description = "Busca un contenedor que cumpla con los requisitos de capacidad especificados.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Contenedor encontrado",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = GetByPesoVolumenResponse.class))),
        @ApiResponse(responseCode = "400", description = "Datos de solicitud inválidos (p.ej. valores no positivos)",
            content = @Content),
        @ApiResponse(responseCode = "404", description = "Contenedor no encontrado para esos criterios",
            content = @Content),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor",
            content = @Content)
    })
    @GetMapping("/getByPesoVolumen")
    public ResponseEntity<GetByPesoVolumenResponse> getByPesoVolumen(
        // CORREGIDO: Se eliminó @RequestBody y se usa @Parameter para documentar los Query Params
        @Parameter(description = "Capacidad de peso requerida", required = true, example = "1000.0")
        @Positive(message = "{error.peso.positive}")
        @Digits(integer = 8, fraction = 2, message = "{error.peso.digits}")
        @RequestParam(value = "capacidadPeso")
        BigDecimal peso,

        @Parameter(description = "Capacidad de volumen requerida", required = true, example = "15.5")
        @Positive(message = "{error.volumen.positive}")
        @Digits(integer = 8, fraction = 2, message = "{error.volumen.digits}")
        @RequestParam(value = "capacidadVolumen") BigDecimal volumen
    ) {
        GetByPesoVolumenRequest request = new GetByPesoVolumenRequest(peso, volumen);

        return ResponseEntity.ok(this.contenedorService.getByPesoVolumen(request));
    }

    @Operation(summary = "Obtener contenedores por estado",
        description = "Devuelve una lista de todos los contenedores que se encuentran en un estado específico.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de contenedores filtrada por estado",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = GetAllResponse.class))),
        @ApiResponse(responseCode = "400", description = "El estado proporcionado no es válido",
            content = @Content),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor",
            content = @Content)
    })
    @GetMapping("/getByEstado/{estado}")
    public ResponseEntity<GetAllResponse> getByEstado(
        @Parameter(description = "Estado por el cual filtrar. Valores permitidos: BORRADOR, PROGRAMADO, EN_TRANSITO, EN_DEPOSITO, ENTREGADO",
            required = true, example = "PROGRAMADO")
        @NotNull(message = "{error.estado.notNull}")
        @PathVariable String estado
    ) {
        GetByEstado request = new GetByEstado(estado);
        return ResponseEntity.ok(this.contenedorService.getByEstado(request));
    }

    @Operation(summary = "Obtener todos los contenedores",
        description = "Devuelve una lista de todos los contenedores registrados en el sistema.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de todos los contenedores",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = GetAllResponse.class))),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor",
            content = @Content)
    })
    @GetMapping("/getAll")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(this.contenedorService.getAll());
    }

    @Operation(summary = "Registrar un nuevo contenedor",
        description = "Crea un nuevo contenedor. Comienza en estado BORRADOR por defecto.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Contenedor registrado exitosamente",
            content = @Content), // Podrías agregar el schema de RegisterResponse si existe
        @ApiResponse(responseCode = "400", description = "Datos inválidos",
            content = @Content),
        @ApiResponse(responseCode = "404", description = "El 'idCliente' proporcionado no existe",
            content = @Content)
    })
    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos del nuevo contenedor", required = true)
        @RequestBody @Valid RegisterRequest request
    ) {
        return ResponseEntity.ok(this.contenedorService.register(request));
    }

    @Operation(summary = "Editar un contenedor existente",
        description = "Actualiza el peso, volumen. El ID es obligatorio.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Contenedor actualizado exitosamente",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = EditResponse.class))),
        @ApiResponse(responseCode = "400", description = "Datos inválidos o ID nulo",
            content = @Content),
        @ApiResponse(responseCode = "404", description = "Contenedor no encontrado",
            content = @Content)
    })
    @PatchMapping("/edit")
    public ResponseEntity<EditResponse> edit(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Campos a editar.", required = true)
        @RequestBody @Valid EditRequest request
    ) {
        return ResponseEntity.ok(this.contenedorService.edit(request));
    }

    @Operation(summary = "Cambiar el estado de un contenedor",
        description = "Realiza una transición de estado (ej: BORRADOR -> PROGRAMADO).")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Estado actualizado",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = CambioDeEstadoResponse.class))),
        @ApiResponse(responseCode = "400", description = "Transición inválida o contenedor entregado",
            content = @Content),
        @ApiResponse(responseCode = "404", description = "Contenedor no encontrado",
            content = @Content)
    })
    @PutMapping("/cambioDeEstado")
    public ResponseEntity<CambioDeEstadoResponse> cambioDeEstado(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "ID y nuevo estado", required = true)
        @RequestBody @Valid CambioDeEstadoRequest request
    ) {
        return ResponseEntity.ok(this.contenedorService.cambioDeEstado(request));
    }

    @Operation(summary = "Asignar un cliente a un contenedor",
        description = "Asigna cliente a un contenedor en estado BORRADOR. Pasa a PROGRAMADO.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cliente asignado",
            content = @Content),
        @ApiResponse(responseCode = "400", description = "Contenedor ya tiene cliente o usuario no es CLIENTE",
            content = @Content),
        @ApiResponse(responseCode = "404", description = "Contenedor o Cliente no encontrado",
            content = @Content)
    })
    @PutMapping("/asignarCliente")
    public ResponseEntity<?> asignarCliente(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "ID Contenedor y ID Cliente", required = true)
        @RequestBody @Valid AsignarClienteRequest request
    ) {
        this.contenedorService.asignarCliente(request);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Eliminar un contenedor por ID",
        description = "Elimina permanentemente un contenedor.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Contenedor eliminado",
            content = @Content),
        @ApiResponse(responseCode = "404", description = "Contenedor no encontrado",
            content = @Content)
    })
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(
        @Parameter(description = "ID del contenedor a eliminar", required = true, example = "1")
        @NotNull(message = "{error.idContenedor.notNull}")
        @Positive(message = "{error.idContenedor.positive}")
        @PathVariable Long id
    ) {
        DeleteRequest request = new DeleteRequest(id);
        this.contenedorService.delete(request);
        return ResponseEntity.ok().build();
    }
}

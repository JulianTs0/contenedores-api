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
        description = "Busca un contenedor que coincida con el peso y/o volumen especificado.")
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
        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Criterios de peso y volumen para la búsqueda", required = true,
            content = @Content(schema = @Schema(implementation = GetByPesoVolumenRequest.class)))
        @Positive(message = "{error.peso.positive}")
        @Digits(integer = 8, fraction = 2, message = "{error.peso.digits}")
        @RequestParam(value = "capacidadPeso")
        BigDecimal peso,

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
        @ApiResponse(responseCode = "400", description = "El estado proporcionado no es válido (Debe ser uno de: BORRADOR, PROGRAMADO, EN_TRANSITO, EN_DEPOSITO, ENTREGADO)",
            content = @Content),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor",
            content = @Content)
    })
    @GetMapping("/getByEstado/{estado}")
    public ResponseEntity<GetAllResponse> getByEstado(
        @Parameter(description = "Estado por el cual filtrar (BORRADOR, PROGRAMADO, EN_TRANSITO, EN_DEPOSITO, ENTREGADO)",
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
        description = "Crea un nuevo contenedor con el peso, volumen y opcionalmente un cliente asignado. Comienza en estado BORRADOR.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Contenedor registrado exitosamente",
            content = @Content),
        @ApiResponse(responseCode = "400", description = "Datos de solicitud inválidos (p.ej. peso/volumen nulos o no positivos)",
            content = @Content),
        @ApiResponse(responseCode = "404", description = "El 'idCliente' proporcionado no existe",
            content = @Content),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor",
            content = @Content)
    })
    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos del nuevo contenedor a registrar", required = true,
            content = @Content(schema = @Schema(implementation = RegisterRequest.class)))
        @RequestBody @Valid RegisterRequest request
    ) {

        return ResponseEntity.ok(this.contenedorService.register(request));
    }

    @Operation(summary = "Editar un contenedor existente",
        description = "Actualiza el peso, volumen o cliente de un contenedor existente. Solo se actualizan los campos proporcionados.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Contenedor actualizado exitosamente",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = EditResponse.class))),
        @ApiResponse(responseCode = "400", description = "Datos de solicitud inválidos (p.ej. ID nulo, valores no positivos)",
            content = @Content),
        @ApiResponse(responseCode = "404", description = "Contenedor ('id') o Cliente ('idCliente') no encontrado",
            content = @Content),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor",
            content = @Content)
    })
    @PatchMapping("/edit")
    public ResponseEntity<EditResponse> edit(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Campos a editar del contenedor. 'id' es obligatorio.", required = true,
            content = @Content(schema = @Schema(implementation = EditRequest.class)))
        @RequestBody @Valid EditRequest request
    ) {
        return ResponseEntity.ok(this.contenedorService.edit(request));
    }

    @Operation(summary = "Cambiar el estado de un contenedor",
        description = "Realiza una transición de estado para un contenedor (p.ej. de BORRADOR a PROGRAMADO). Valida la lógica de transiciones.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Estado del contenedor actualizado",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = CambioDeEstadoResponse.class))),
        @ApiResponse(responseCode = "400", description = "Transición de estado inválida, estado no válido, o contenedor ya entregado",
            content = @Content),
        @ApiResponse(responseCode = "404", description = "Contenedor no encontrado",
            content = @Content),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor",
            content = @Content)
    })
    @PutMapping("/cambioDeEstado")
    public ResponseEntity<CambioDeEstadoResponse> cambioDeEstado(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "ID del contenedor y el nuevo estado deseado", required = true,
            content = @Content(schema = @Schema(implementation = CambioDeEstadoRequest.class)))
        @RequestBody @Valid CambioDeEstadoRequest request
    ) {
        return ResponseEntity.ok(this.contenedorService.cambioDeEstado(request));
    }

    @Operation(summary = "Asignar un cliente a un contenedor",
        description = "Asigna un cliente a un contenedor que esté en estado BORRADOR y sin cliente. El contenedor pasa a estado PROGRAMADO.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cliente asignado exitosamente al contenedor",
            content = @Content),
        @ApiResponse(responseCode = "400", description = "El contenedor ya tiene un cliente asignado, o el usuario no es de rol 'CLIENTE'",
            content = @Content),
        @ApiResponse(responseCode = "404", description = "Contenedor o Cliente no encontrado",
            content = @Content),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor",
            content = @Content)
    })
    @PutMapping("/asignarCliente")
    public ResponseEntity<?> asignarCliente(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "ID del contenedor y ID del cliente a asignar", required = true,
            content = @Content(schema = @Schema(implementation = AsignarClienteRequest.class)))
        @RequestBody @Valid AsignarClienteRequest request
    ) {
        this.contenedorService.asignarCliente(request);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Eliminar un contenedor por ID",
        description = "Elimina un contenedor de la base de datos permanentemente.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Contenedor eliminado exitosamente",
            content = @Content),
        @ApiResponse(responseCode = "400", description = "ID proporcionado inválido (debe ser positivo)",
            content = @Content),
        @ApiResponse(responseCode = "404", description = "Contenedor no encontrado",
            content = @Content),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor",
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

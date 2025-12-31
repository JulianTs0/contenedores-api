package backend.grupo130.camiones.controller;

import backend.grupo130.camiones.dto.CamionesMapperDto;
import backend.grupo130.camiones.dto.request.*;
import backend.grupo130.camiones.dto.response.*;
import backend.grupo130.camiones.service.CamionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/camiones")
@RequiredArgsConstructor
@Validated
@Data
@Tag(name = "Gestión de Camiones", description = "API para la administración de flota, asignación de transportistas y cálculo de costos.")
public class CamionController {

    private final CamionService camionService;

    @Operation(
        summary = "Obtener camión por dominio",
        description = "Busca y devuelve un camión específico usando su dominio (patente) como identificador único."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Camión encontrado exitosamente",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = GetByIdResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Camión no encontrado",
            content = @Content),
        @ApiResponse(
            responseCode = "500",
            description = "Error interno del servidor",
            content = @Content
        )
    })
    @GetMapping("/{dominio}")
    public ResponseEntity<GetByIdResponse> getById(
        @Parameter(description = "Dominio (patente) del camión. Ejemplo: AA123BB", required = true, example = "AA123BB")
        @NotBlank(message = "{error.dominio.path.notBlank}")
        @Size(max = 80, message = "{error.dominio.max}")
        @PathVariable String dominio
    ) {
        GetByIdRequest request = new GetByIdRequest(dominio);
        return ResponseEntity.ok(this.camionService.getById(request));
    }

    @Operation(
        summary = "Obtener todos los camiones",
        description = "Devuelve el listado completo de la flota de camiones registrada en el sistema."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Lista recuperada exitosamente",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = GetAllResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Error interno del servidor",
            content = @Content
        )
    })
    @GetMapping("/")
    public ResponseEntity<GetAllResponse> getAll() {
        return ResponseEntity.ok(this.camionService.getAll());
    }

    @Operation(
        summary = "Obtener camiones disponibles",
        description = "Devuelve una lista de camiones cuyo estado es 'activo' (true) y están listos para asignaciones."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Lista de disponibles recuperada exitosamente",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = GetAllResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Error interno del servidor",
            content = @Content
        )
    })
    @GetMapping("/disponibles")
    public ResponseEntity<GetAllResponse> getDisponibles() {
        return ResponseEntity.ok(this.camionService.getDisponibles());
    }

    @Operation(
        summary = "Obtener consumo promedio de combustible",
        description = "Calcula el promedio global de consumo de combustible basado en toda la flota registrada."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Cálculo exitoso",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = GetPromedioCombustibleActualResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Error interno del servidor",
            content = @Content
        )
    })
    @GetMapping("/consumo/promedio")
    public ResponseEntity<GetPromedioCombustibleActualResponse> getConsumoPromedio() {
        return ResponseEntity.ok(this.camionService.getConsumoPromedio());
    }

    @Operation(
        summary = "Registrar un nuevo camión",
        description = "Da de alta una nueva unidad en la flota. El estado inicial será 'activo' por defecto."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Camión creado exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = RegisterResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Datos inválidos (ej. dominio vacío, valores negativos)",
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
        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos del nuevo camión", required = true)
        @RequestBody @Valid RegisterRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.camionService.register(request));
    }

    @Operation(
        summary = "Obtener costo aproximado para envío",
        description = "Calcula el costo base promedio basado en los 3 camiones más adecuados para la carga solicitada."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Cálculo exitoso",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = GetPromedioCostoBaseResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Parámetros inválidos (valores negativos o nulos)",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Error interno del servidor",
            content = @Content
        )
    })
    @GetMapping("/costo/promedio")
    public ResponseEntity<GetPromedioCostoBaseResponse> getCostoPromedio(
        @Parameter(description = "Peso total de la carga en kg", required = true, example = "1500.0")
        @NotNull(message = "{error.capacidadPeso.notNull}")
        @Positive(message = "{error.capacidadPeso.positive}")
        @Digits(integer = 10, fraction = 2, message = "{error.capacidadPeso.digits}")
        @RequestParam(value = "peso") BigDecimal peso,

        @Parameter(description = "Volumen total de la carga en m3", required = true, example = "20.5")
        @NotNull(message = "{error.capacidadVolumen.notNull}")
        @Positive(message = "{error.capacidadVolumen.positive}")
        @Digits(integer = 10, fraction = 2, message = "{error.capacidadVolumen.digits}")
        @RequestParam(value = "volumen") BigDecimal volumen
    ) {

        GetCostoPromedio request = new GetCostoPromedio(peso, volumen);
        return ResponseEntity.ok(this.camionService.getCostoAprox(request));
    }

    @Operation(
        summary = "Editar un camión",
        description = "Actualiza los datos técnicos de un camión. Requiere que el camión esté disponible (estado = true)."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Actualización exitosa",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = EditResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Datos inválidos o camión no disponible",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Camión no encontrado",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Error interno del servidor",
            content = @Content
        )
    })
    @PatchMapping("/{dominio}")
    public ResponseEntity<EditResponse> edit(
        @NotBlank(message = "{error.dominio.path.notBlank}")
        @Size(max = 80, message = "{error.dominio.max}")
        @PathVariable String dominio,
        @RequestBody @Valid EditRequest body
    ) {
        EditRequest request = CamionesMapperDto.toRequestPatchEdit(dominio, body);

        return ResponseEntity.ok(this.camionService.edit(request));
    }

    @Operation(
        summary = "Cambiar disponibilidad",
        description = "Activa o desactiva un camión (Soft Delete lógico o mantenimiento)."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Estado modificado exitosamente",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = EditResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Camión no encontrado",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Error interno del servidor",
            content = @Content
        )
    })
    @PutMapping("/disponibilidad/{dominio}")
    public ResponseEntity<CambiarDisponibilidadResponse> cambiarDisponibilidad(
        @NotBlank(message = "{error.dominio.path.notBlank}")
        @Size(max = 80, message = "{error.dominio.max}")
        @PathVariable String dominio,
        @RequestBody @Valid CambiarDisponibilidadRequest body
    ) {
        CambiarDisponibilidadRequest request = CamionesMapperDto.toRequestPatchDispo(dominio, body);

        return ResponseEntity.ok(this.camionService.cambiarDisponibilidad(request));
    }

    @Operation(
        summary = "Asignar transportista",
        description = "Vincula un usuario con rol TRANSPORTISTA a un camión disponible."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Asignación exitosa",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Camión no disponible o usuario no es transportista",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Camión o Usuario no encontrado",
            content = @Content
        )
    })
    @PatchMapping("/transportista/{dominio}")
    public ResponseEntity<AsignarTransportistaResponse> asignarTransportista(
        @NotBlank(message = "{error.dominio.path.notBlank}")
        @Size(max = 80, message = "{error.dominio.max}")
        @PathVariable String dominio,
        @RequestBody @Valid AsignarTransportistaRequest body
    ) {
        AsignarTransportistaRequest request = CamionesMapperDto.toRequestPatchTrans(dominio, body);

        return ResponseEntity.ok(this.camionService.asignarTransportista(request));
    }

    @Operation(
        summary = "Eliminar camión",
        description = "Elimina permanentemente un camión de la base de datos. Solo permitido si está disponible."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Eliminación exitosa",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "400",
            description = "No se puede eliminar (camión no disponible)",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Camión no encontrado",
            content = @Content
        )
    })
    @DeleteMapping("/{dominio}")
    public ResponseEntity<Void> delete(
        @Parameter(description = "Dominio del camión a eliminar", required = true, example = "AA123BB")
        @NotBlank(message = "{error.dominio.path.notBlank}")
        @Size(max = 80, message = "{error.dominio.max}")
        @PathVariable String dominio
    ) {
        DeleteRequest request = new DeleteRequest(dominio);
        this.camionService.delete(request);
        return ResponseEntity.ok().build();
    }
}

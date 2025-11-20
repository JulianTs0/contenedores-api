package backend.grupo130.camiones.controller;

import backend.grupo130.camiones.data.models.Camion;
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
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/camiones")
@RequiredArgsConstructor
@Validated
@Data
@Tag(name = "Gestión de Camiones", description = "API para la administración de camiones")
public class CamionController {

    private final CamionService camionService;

    @Operation(
        summary = "Obtener camión por dominio",
        description = "Busca y devuelve un camión específico usando su dominio (patente) como identificador."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Camión encontrado exitosamente",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = GetByIdResponse.class))),
        @ApiResponse(responseCode = "404", description = "Camión no encontrado", content = @Content),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @GetMapping("/getById/{dominio}")
    public ResponseEntity<GetByIdResponse> getById(
        @Parameter(description = "Dominio (patente) del camión a buscar", required = true, example = "AA123BB")
        @NotBlank(message = "{error.dominio.notBlank}")
        @Size(max = 80, message = "{error.dominio.max}")
        @PathVariable String dominio
    ) {
        GetByIdRequest request = new GetByIdRequest(dominio);
        return ResponseEntity.ok(this.camionService.getById(request));
    }

    @Operation(
        summary = "Obtener todos los camiones",
        description = "Devuelve una lista de todos los camiones registrados en el sistema."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de camiones devuelta exitosamente",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = GetAllResponse.class))),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @GetMapping("/getAll")
    public ResponseEntity<GetAllResponse> getAll() {
        return ResponseEntity.ok(this.camionService.getAll());
    }

    @Operation(
        summary = "Obtener camiones disponibles",
        description = "Devuelve una lista de todos los camiones que están actualmente disponibles (estado = true)."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de camiones disponibles devuelta exitosamente",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = GetAllResponse.class))),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @GetMapping("/getDisponibles")
    public ResponseEntity<GetAllResponse> getDisponibles() {
        return ResponseEntity.ok(this.camionService.getDisponibles());
    }

    @Operation(
        summary = "Obtener consumo promedio de combustible",
        description = "Calcula y devuelve el consumo promedio de combustible de todos los camiones."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Consumo promedio calculado exitosamente",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = GetPromedioCombustibleActualResponse.class))),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @GetMapping("/getConsumoPromedio")
    public ResponseEntity<GetPromedioCombustibleActualResponse> getConsumoPromedio() {
        return ResponseEntity.ok(this.camionService.getConsumoPromedio());
    }

    @Operation(
        summary = "Registrar un nuevo camión",
        description = "Crea un nuevo camión en el sistema con los datos proporcionados."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Camión registrado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RegisterResponse.class))),
        @ApiResponse(responseCode = "400", description = "Datos de registro inválidos (Campos faltantes o formato incorrecto)", content = @Content),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos del nuevo camión", required = true)
        @RequestBody @Valid RegisterRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.camionService.register(request));
    }

    @Operation(
        summary = "Obtener costo aproximado para envío",
        description = "Calcula el costo base promedio de los 3 camiones más adecuados según la capacidad de peso y volumen requerida."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Costo aproximado devuelto exitosamente",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = GetPromedioCostoBaseResponse.class))),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos", content = @Content),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @GetMapping("/getCostoPromedio")
    public ResponseEntity<GetPromedioCostoBaseResponse> getCostoPromedio(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Capacidades requeridas para el envío", required = true)
        @NotNull(message = "{error.capacidadPeso.notNull}")
        @Positive(message = "{error.capacidadPeso.positive}")
        @RequestParam(value = "capacidadPeso") BigDecimal capacidadPeso,

        @NotNull(message = "{error.capacidadVolumen.notNull}")
        @Positive(message = "{error.capacidadVolumen.positive}")
        @RequestParam(value = "capacidadVolumen") BigDecimal capacidadVolumen
    ) {

        GetCostoPromedio request = new GetCostoPromedio(capacidadPeso, capacidadVolumen);

        return ResponseEntity.ok(this.camionService.getCostoAprox(request));
    }

    @Operation(
        summary = "Editar un camión",
        description = "Actualiza los datos de un camión existente. Solo se pueden editar camiones que estén disponibles (estado = true)."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Camión editado exitosamente",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = EditResponse.class))),
        @ApiResponse(responseCode = "400", description = "Datos de edición inválidos o el camión no está disponible", content = @Content),
        @ApiResponse(responseCode = "404", description = "Camión no encontrado", content = @Content),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @PatchMapping("/edit")
    public ResponseEntity<EditResponse> edit(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos a modificar del camión. El dominio es obligatorio, el resto son opcionales.", required = true)
        @RequestBody @Valid EditRequest request
    ) {
        return ResponseEntity.ok(this.camionService.edit(request));
    }

    @Operation(
        summary = "Cambiar disponibilidad de un camión",
        description = "Modifica el estado de disponibilidad (activo/inactivo) de un camión."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Disponibilidad cambiada exitosamente",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = EditResponse.class))),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos", content = @Content),
        @ApiResponse(responseCode = "404", description = "Camión no encontrado", content = @Content),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @PutMapping("/cambiarDisponibilidad")
    public ResponseEntity<EditResponse> cambiarDisponibilidad(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Dominio del camión y su nuevo estado de disponibilidad", required = true)
        @RequestBody @Valid CambiarDisponibilidadRequest request
    ) {
        return ResponseEntity.ok(this.camionService.cambiarDisponibilidad(request));
    }

    @Operation(
        summary = "Asignar un transportista a un camión",
        description = "Asigna un transportista (usuario con rol 'TRANSPORTISTA') a un camión que esté disponible."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Transportista asignado exitosamente", content = @Content),
        @ApiResponse(responseCode = "400", description = "Datos inválidos, camión no disponible o el usuario no es un transportista", content = @Content),
        @ApiResponse(responseCode = "404", description = "Camión y/o Usuario no encontrado", content = @Content),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @PatchMapping("/asignarTransportista")
    public ResponseEntity<?> asignarTransportista(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Dominio del camión e ID del transportista a asignar", required = true)
        @RequestBody @Valid AsignarTransportistaRequest request
    ) {
        this.camionService.asignarTransportista(request);
        return ResponseEntity.ok().build();
    }

    @Operation(
        summary = "Eliminar un camión",
        description = "Elimina un camión del sistema usando su dominio. Solo se pueden eliminar camiones que estén disponibles."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Camión eliminado exitosamente", content = @Content),
        @ApiResponse(responseCode = "400", description = "El camión no está disponible y no puede ser eliminado", content = @Content),
        @ApiResponse(responseCode = "404", description = "Camión no encontrado", content = @Content),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @DeleteMapping("/delete/{dominio}")
    public ResponseEntity<?> delete(
        @Parameter(description = "Dominio (patente) del camión a eliminar", required = true, example = "AA123BB")
        @NotBlank(message = "{error.dominio.notBlank}")
        @Size(max = 80, message = "{error.dominio.max}")
        @PathVariable String dominio
    ) {
        DeleteRequest request = new DeleteRequest(dominio);
        this.camionService.delete(request);
        return ResponseEntity.ok().build();
    }
}

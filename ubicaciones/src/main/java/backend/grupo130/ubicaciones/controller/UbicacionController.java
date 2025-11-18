
package backend.grupo130.ubicaciones.controller;

import backend.grupo130.ubicaciones.data.models.Ubicacion;
import backend.grupo130.ubicaciones.dto.ubicaciones.request.UbicacionDeleteRequest;
import backend.grupo130.ubicaciones.dto.ubicaciones.request.UbicacionEditRequest;
import backend.grupo130.ubicaciones.dto.ubicaciones.request.UbicacionGetByIdRequest;
import backend.grupo130.ubicaciones.dto.ubicaciones.request.UbicacionRegisterRequest;
import backend.grupo130.ubicaciones.dto.ubicaciones.response.UbicacionEditResponse;
import backend.grupo130.ubicaciones.dto.ubicaciones.response.UbicacionGetAllResponse;
import backend.grupo130.ubicaciones.dto.ubicaciones.response.UbicacionGetByIdResponse;
import backend.grupo130.ubicaciones.service.UbicacionService;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ubicaciones/ubicaciones")
@RequiredArgsConstructor
@Validated
@Slf4j
@Tag(name = "Gestión de Ubicaciones", description = "Endpoints para crear, leer, actualizar y eliminar Ubicaciones.")
public class UbicacionController {

    private final UbicacionService ubicacionService;

    @Operation(summary = "Obtener una ubicación por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Ubicación encontrada",
            content = { @Content(mediaType = "application/json",
                schema = @Schema(implementation = UbicacionGetByIdResponse.class)) }),
        @ApiResponse(responseCode = "400", description = "ID inválido (no es positivo)", content = @Content),
        @ApiResponse(responseCode = "404", description = "Ubicación no encontrada", content = @Content),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @GetMapping("/getById/{id}")
    public ResponseEntity<UbicacionGetByIdResponse> getById(
        @Parameter(description = "ID de la ubicación a buscar", required = true, example = "1")
        @NotNull(message = "{error.idUbicacion.notNull}")
        @Positive(message = "{error.idUbicacion.positive}")
        @PathVariable Long id
    ) {
        log.info("Iniciando getById para Ubicacion con ID: {}", id);
        UbicacionGetByIdRequest request = new UbicacionGetByIdRequest(id);
        UbicacionGetByIdResponse response = this.ubicacionService.getById(request);
        log.info("Finalizado getById para Ubicacion con ID: {}", id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Obtener todas las ubicaciones")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Listado de ubicaciones",
            content = { @Content(mediaType = "application/json",
                schema = @Schema(implementation = UbicacionGetAllResponse.class)) }),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @GetMapping("/getAll")
    public ResponseEntity<UbicacionGetAllResponse> getAll() {
        log.info("Iniciando getAll para Ubicaciones");
        UbicacionGetAllResponse response = this.ubicacionService.getAll();
        log.info("Finalizado getAll para Ubicaciones, total encontradas: {}", response.getUbicaciones().size());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Registrar una nueva ubicación")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Ubicación registrada exitosamente", content = @Content),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos (latitud, longitud, dirección)", content = @Content),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @PostMapping("/register")
    public ResponseEntity<?> register(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos de la nueva ubicación", required = true,
            content = @Content(schema = @Schema(implementation = UbicacionRegisterRequest.class)))
        @RequestBody @Valid UbicacionRegisterRequest request
    ) {
        log.info("Iniciando register para nueva Ubicacion con direccion: {}", request.getDireccion());
        this.ubicacionService.register(request);
        log.info("Finalizado register para nueva Ubicacion");
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Editar una ubicación existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Ubicación editada exitosamente",
            content = { @Content(mediaType = "application/json",
                schema = @Schema(implementation = UbicacionEditResponse.class)) }),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos", content = @Content),
        @ApiResponse(responseCode = "404", description = "Ubicación no encontrada", content = @Content),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @PatchMapping("/edit")
    public ResponseEntity<UbicacionEditResponse> edit(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos a modificar de la ubicación. El ID es obligatorio.", required = true,
            content = @Content(schema = @Schema(implementation = UbicacionEditRequest.class)))
        @RequestBody @Valid UbicacionEditRequest request
    ) {
        log.info("Iniciando edit para Ubicacion con ID: {}", request.getIdUbicacion());
        UbicacionEditResponse response = this.ubicacionService.edit(request);
        log.info("Finalizado edit para Ubicacion con ID: {}", request.getIdUbicacion());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Eliminar una ubicación por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Ubicación eliminada exitosamente", content = @Content),
        @ApiResponse(responseCode = "400", description = "ID inválido (no es positivo)", content = @Content),
        @ApiResponse(responseCode = "404", description = "Ubicación no encontrada", content = @Content),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(
        @Parameter(description = "ID de la ubicación a eliminar", required = true, example = "1")
        @NotNull(message = "{error.idUbicacion.notNull}")
        @Positive(message = "{error.idUbicacion.positive}")
        @PathVariable Long id
    ) {
        log.info("Iniciando delete para Ubicacion con ID: {}", id);
        UbicacionDeleteRequest request = new UbicacionDeleteRequest(id);
        this.ubicacionService.delete(request);
        log.info("Finalizado delete para Ubicacion con ID: {}", id);
        return ResponseEntity.ok().build();
    }

}


package backend.grupo130.ubicaciones.controller;

import backend.grupo130.ubicaciones.data.entity.Ubicacion;
import backend.grupo130.ubicaciones.dto.ubicaciones.UbicacionesMapperDto;
import backend.grupo130.ubicaciones.dto.ubicaciones.request.UbicacionEditRequest;
import backend.grupo130.ubicaciones.dto.ubicaciones.request.UbicacionGetByIdRequest;
import backend.grupo130.ubicaciones.dto.ubicaciones.request.UbicacionGetListByIdRequest;
import backend.grupo130.ubicaciones.dto.ubicaciones.request.UbicacionRegisterRequest;
import backend.grupo130.ubicaciones.dto.ubicaciones.response.*;
import backend.grupo130.ubicaciones.service.UbicacionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ubicaciones/ubicaciones")
@RequiredArgsConstructor
@Validated
@Slf4j
@Tag(name = "Ubicaciones", description = "Endpoints para la gestión de Ubicaciones.")
public class UbicacionController {

    private final UbicacionService ubicacionService;

    @Operation(
        summary = "Obtener una Ubicacion por su ID",
        description = "Devuelve una única Ubicacion.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Ubicacion encontrada.",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UbicacionGetByIdResponse.class)
                )
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Ubicacion no encontrada."
            )
        }
    )
    @GetMapping("/{id}")
    public ResponseEntity<UbicacionGetByIdResponse> getById(
        @PathVariable
        @NotNull(message = "{error.idUbicacion.notNull}")
        @Positive(message = "{error.idUbicacion.positive}")
        Long id
    ) {
        UbicacionGetByIdRequest request = new UbicacionGetByIdRequest(id);
        return ResponseEntity.ok(this.ubicacionService.getById(request));
    }

    @Operation(
        summary = "Obtener todas las Ubicaciones",
        description = "Devuelve una lista de todas las Ubicaciones.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Lista de Ubicaciones.",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                        implementation = UbicacionGetAllResponse.class
                    )
                )
            )
        }
    )
    @GetMapping("/")
    public ResponseEntity<UbicacionGetAllResponse> getAll() {
        return ResponseEntity.ok(this.ubicacionService.getAll());
    }

    @GetMapping("/lista")
    public ResponseEntity<UbicacionGetListByIdResponse> getByListIds(
        @RequestParam(value = "ids")
        @NotEmpty(message = "{error.listaIds.notEmpty}")
        List<Long> ids
    ) {
        UbicacionGetListByIdRequest request = UbicacionesMapperDto.toRequestGetList(ids);
        return ResponseEntity.ok(this.ubicacionService.getByListIds(request));
    }

    @Operation(
        summary = "Registrar una nueva Ubicacion",
        description = "Crea una nueva Ubicacion y la devuelve.",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Datos de la nueva Ubicacion.",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = UbicacionRegisterRequest.class)
            )
        ),
        responses = {
            @ApiResponse(
                responseCode = "201",
                description = "Ubicacion creada exitosamente.",
                headers = @Header(
                    name = "Location",
                    description = "URI de la nueva Ubicacion."
                ),
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UbicacionRegisterResponse.class)
                )
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Solicitud inválida."
            )
        }
    )
    @PostMapping("/")
    public ResponseEntity<UbicacionRegisterResponse> register(
        @RequestBody @Valid UbicacionRegisterRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.ubicacionService.register(request));
    }

    @Operation(
        summary = "Actualizar una Ubicacion existente",
        description = "Actualiza una Ubicacion existente y la devuelve.",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Datos para actualizar la Ubicacion.",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = UbicacionEditRequest.class)
            )
        ),
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Ubicacion actualizada exitosamente.",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UbicacionEditResponse.class)
                )
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Ubicacion no encontrada."
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Solicitud inválida."
            )
        }
    )
    @PatchMapping("/{id}")
    public ResponseEntity<UbicacionEditResponse> edit(
        @PathVariable
        @NotNull(message = "{error.idUbicacion.notNull}")
        @Positive(message = "{error.idUbicacion.positive}")
        Long id,
        @RequestBody @Valid UbicacionEditRequest body
    ) {
        UbicacionEditRequest request = UbicacionesMapperDto.toRequestPatchEdit(id, body);
        return ResponseEntity.ok(this.ubicacionService.edit(request));
    }

    @Operation(
        summary = "Eliminar una Ubicacion por su ID",
        description = "Elimina una Ubicacion existente.",
        responses = {
            @ApiResponse(
                responseCode = "204",
                description = "Ubicacion eliminada exitosamente."
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Ubicacion no encontrada."
            )
        }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
        @PathVariable
        @NotNull(message = "{error.idUbicacion.notNull}")
        @Positive(message = "{error.idUbicacion.positive}")
        Long id
    ) {
        this.ubicacionService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

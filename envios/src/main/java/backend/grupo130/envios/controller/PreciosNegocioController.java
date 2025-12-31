package backend.grupo130.envios.controller;

import backend.grupo130.envios.dto.precios.PreciosNegocioMapperDto;
import backend.grupo130.envios.dto.precios.request.PreciosNegocioCreateRequest;
import backend.grupo130.envios.dto.precios.request.PreciosNegocioGetByIdRequest;
import backend.grupo130.envios.dto.precios.request.PreciosNegocioUpdateRequest;
import backend.grupo130.envios.dto.precios.response.*;
import backend.grupo130.envios.service.PreciosNegocioService;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/envios/precios")
@RequiredArgsConstructor
@Slf4j
@Validated
@Tag(name = "Gestión de Precios de Negocio", description = "API para la administración de precios y configuraciones de negocio.")
public class PreciosNegocioController {

    private final PreciosNegocioService preciosNegocioService;

    @Operation(
            summary = "Obtener precios de negocio por ID",
            description = "Busca y devuelve el detalle completo de los precios de negocio específicos."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Precios encontrados exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PreciosNegocioGetByIdResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Precios no encontrados",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<PreciosNegocioGetByIdResponse> getById(
            @Parameter(description = "ID único de los precios a consultar", required = true, example = "1")
            @NotNull(message = "{error.idPreciosNegocio.notNull}")
            @Positive(message = "{error.idPreciosNegocio.positive}")
            @PathVariable Long id
    ) {
        log.info("Recibida solicitud GET en /api/envios/precios-negocio/{}", id);
        PreciosNegocioGetByIdRequest request = new PreciosNegocioGetByIdRequest(id);
        return ResponseEntity.ok(this.preciosNegocioService.getById(request));
    }

    @Operation(
            summary = "Obtener los precios de negocio más recientes",
            description = "Devuelve la configuración de precios de negocio más actual registrada en el sistema."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Precios más recientes encontrados exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PreciosNegocioGetLatestResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No hay precios registrados",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content
            )
    })
    @GetMapping("/ultimo")
    public ResponseEntity<PreciosNegocioGetLatestResponse> getLatest() {
        log.info("Recibida solicitud GET en /api/envios/precios-negocio/latest");
        return ResponseEntity.ok(this.preciosNegocioService.getLatest());
    }

    @Operation(
            summary = "Obtener todos los precios de negocio",
            description = "Devuelve un listado completo de todos los historiales de precios de negocio."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Listado recuperado exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PreciosNegocioGetAllResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content
            )
    })
    @GetMapping("/")
    public ResponseEntity<PreciosNegocioGetAllResponse> getAll() {
        log.info("Recibida solicitud GET en /api/envios/precios-negocio/");
        return ResponseEntity.ok(this.preciosNegocioService.getAll());
    }

    @Operation(
            summary = "Crear nuevos precios de negocio",
            description = "Registra una nueva configuración de precios de negocio en el sistema."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Precios creados exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PreciosNegocioCreateResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos inválidos",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content
            )
    })
    @PostMapping("/")
    public ResponseEntity<PreciosNegocioCreateResponse> create(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos de los nuevos precios", required = true)
            @RequestBody @Valid PreciosNegocioCreateRequest request
    ) {
        log.info("Recibida solicitud POST en /api/envios/precios-negocio/");
        return ResponseEntity.status(HttpStatus.CREATED).body(this.preciosNegocioService.create(request));
    }

    @Operation(
            summary = "Actualizar precios de negocio",
            description = "Modifica una configuración de precios de negocio existente."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Precios actualizados exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PreciosNegocioUpdateResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos inválidos",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Precios no encontrados",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<PreciosNegocioUpdateResponse> update(
            @Parameter(description = "ID único de los precios a actualizar", required = true, example = "1")
            @NotNull(message = "{error.idPreciosNegocio.notNull}")
            @Positive(message = "{error.idPreciosNegocio.positive}")
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos actualizados", required = true)
            @RequestBody @Valid PreciosNegocioUpdateRequest body
    ) {
        log.info("Recibida solicitud PUT en /api/envios/precios-negocio/{}", id);
        PreciosNegocioUpdateRequest request = PreciosNegocioMapperDto.toPreciosNegocioUpdateRequest(id, body);
        return ResponseEntity.ok(this.preciosNegocioService.update(request));
    }

    @Operation(
            summary = "Eliminar precios de negocio",
            description = "Elimina una configuración de precios de negocio del sistema."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Precios eliminados exitosamente",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Precios no encontrados",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID único de los precios a eliminar", required = true, example = "1")
            @NotNull(message = "{error.idPreciosNegocio.notNull}")
            @Positive(message = "{error.idPreciosNegocio.positive}")
            @PathVariable Long id) {
        log.info("Recibida solicitud DELETE en /api/envios/precios-negocio/{}", id);
        PreciosNegocioGetByIdRequest request = new PreciosNegocioGetByIdRequest(id);
        this.preciosNegocioService.delete(request);
        return ResponseEntity.noContent().build();
    }
}

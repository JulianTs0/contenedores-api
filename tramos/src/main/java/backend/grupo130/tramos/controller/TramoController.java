package backend.grupo130.tramos.controller;

import backend.grupo130.tramos.dto.tramo.TramoMapperDto;
import backend.grupo130.tramos.dto.tramo.request.*;
import backend.grupo130.tramos.dto.tramo.response.TramoGetAllResponse;
import backend.grupo130.tramos.dto.tramo.response.TramoGetByIdResponse;
import backend.grupo130.tramos.service.TramoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tramos/tramos")
@RequiredArgsConstructor
@Validated
@Slf4j
@Tag(name = "Gestión de Tramos", description = "API para la consulta y actualización de estado de los tramos (segmentos de una ruta).")
public class TramoController {

    private final TramoService tramoService;

    @Operation(
        summary = "Obtener un tramo por su ID",
        description = "Busca y devuelve un tramo específico utilizando su ID único, incluyendo detalles del camión y ubicaciones si están asignados."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "TramoModel encontrado exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = TramoGetByIdResponse.class))),
        @ApiResponse(
            responseCode = "404",
            description = "TramoModel no encontrado",
            content = @Content),
        @ApiResponse(
            responseCode = "500",
            description = "Error interno del servidor",
            content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<TramoGetByIdResponse> getById(
        @Parameter(description = "ID único del tramo a buscar", required = true, example = "1")
        @NotNull(message = "{error.idTramo.notNull}")
        @Positive(message = "{error.idTramo.positive}")
        @PathVariable Long id
    ) {
        log.info("Iniciando GET /api/tramos/{}", id);
        TramoGetByIdRequest request = new TramoGetByIdRequest(id);
        return ResponseEntity.ok(this.tramoService.getById(request));
    }

    @Operation(
        summary = "Obtener todos los tramos",
        description = "Devuelve una lista completa de todos los tramos registrados en el sistema."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Lista de tramos obtenida exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = TramoGetAllResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Error interno del servidor",
            content = @Content
        )
    })
    @GetMapping("/")
    public ResponseEntity<TramoGetAllResponse> getAll() {
        log.info("Iniciando GET /api/tramos/getAll");
        return ResponseEntity.ok(this.tramoService.getAll());
    }

    @Operation(
        summary = "Asignar un camión a un tramo",
        description = "Asigna un camión disponible a un tramo en estado 'ESTIMADO'. Si se completan todos los tramos, la solicitud avanza a 'PROGRAMADO'."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Camión asignado exitosamente",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "400",
            description = "TramoModel no está en estado 'ESTIMADO' o camión no disponible",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "404",
            description = "TramoModel o Camión no encontrado",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Error interno del servidor",
            content = @Content
        )
    })
    @PatchMapping("/camion/{id}")
    public ResponseEntity<?> asignarCamion(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos de asignación (ID TramoModel + Dominio Camión)", required = true)
        @NotNull(message = "{error.idTramo.notNull}")
        @Positive(message = "{error.idTramo.positive}")
        @PathVariable Long id,
        @RequestBody @Valid TramoAsignacionCamionRequest body
    ) {
        TramoAsignacionCamionRequest request = TramoMapperDto.toRequestPatchCamion(id, body);
        this.tramoService.asignarCamion(request);
        return ResponseEntity.ok().build();
    }

    @Operation(
        summary = "Obtener tramos por transportista (camión)",
        description = "Devuelve todos los tramos asignados a un camión específico."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Lista de tramos obtenida exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = TramoGetAllResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Error interno del servidor",
            content = @Content
        )
    })
    @GetMapping("/transportista/{dominio}")
    public ResponseEntity<TramoGetAllResponse> getByTransportista(
        @Parameter(description = "Dominio (patente) del camión", required = true, example = "AA123BB")
        @NotBlank(message = "{error.dominioCamion.notBlank}")
        @PathVariable String dominio
    ) {
        log.info("Iniciando GET /api/tramos/getByTransportista/{}", dominio);
        TramoGetByTransportistaRequest request = TramoMapperDto.toRequestGetTransportista(dominio);
        return ResponseEntity.ok(this.tramoService.getByTransportista(request));
    }

    @Operation(
        summary = "Obtener todos los tramos de una ruta",
        description = "Devuelve la secuencia de tramos que componen una ruta específica."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Lista de tramos obtenida exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = TramoGetAllResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Ruta no encontrada",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Error interno del servidor",
            content = @Content
        )
    })
    @GetMapping("/ruta/{id}")
    public ResponseEntity<TramoGetAllResponse> getByRuta(
        @Parameter(description = "ID de la ruta padre", required = true, example = "1")
        @NotNull(message = "{error.idRuta.notNull}")
        @Positive(message = "{error.idRuta.positive}")
        @PathVariable Long id
    ) {
        log.info("Iniciando GET /api/tramos/getByRuta/{}", id);
        TramoGetByRutaIdRequest request = TramoMapperDto.toRequestGetRuta(id);
        return ResponseEntity.ok(this.tramoService.getTramosDeRuta(request));
    }

    @Operation(
        summary = "Registrar el inicio de un tramo",
        description = "Marca un tramo como 'INICIADO'. Requiere que el tramo esté 'ASIGNADO'. Actualiza el estado de la solicitud si corresponde."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Inicio de tramo registrado",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Estado incorrecto (no ASIGNADO) o secuencia inválida",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "403",
            description = "No autorizado (camión incorrecto)",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "404",
            description = "TramoModel o Camión no encontrado",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Error interno del servidor",
            content = @Content
        )
    })
    @PatchMapping("/iniciar/{id}")
    public ResponseEntity<?> registrarInicio(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos para iniciar tramo (ID + Dominio)", required = true)
        @NotNull(message = "{error.idTramo.notNull}")
        @Positive(message = "{error.idTramo.positive}")
        @PathVariable Long id,
        @RequestBody @Valid TramoInicioTramoRequest body
    ) {
        TramoInicioTramoRequest request = TramoMapperDto.toRequestPatchInicio(id, body);
        this.tramoService.registrarInicioTramo(request);
        return ResponseEntity.ok().build();
    }

    @Operation(
        summary = "Registrar el fin de un tramo",
        description = "Marca un tramo como 'FINALIZADO'. Requiere que esté 'INICIADO'. Si es el último tramo, finaliza la solicitud."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Fin de tramo registrado",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Estado incorrecto (no INICIADO)",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "403",
            description = "No autorizado (camión incorrecto)",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "404",
            description = "TramoModel o Camión no encontrado",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Error interno del servidor",
            content = @Content
        )
    })
    @PatchMapping("/finalizar/{id}")
    public ResponseEntity<?> registrarFin(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos para finalizar tramo (ID + Dominio)", required = true)
        @NotNull(message = "{error.idTramo.notNull}")
        @Positive(message = "{error.idTramo.positive}")
        @PathVariable Long id,
        @RequestBody @Valid TramoFinTramoRequest body
    ) {
        TramoFinTramoRequest request = TramoMapperDto.toRequestPatchFin(id, body);
        this.tramoService.registrarFinTramo(request);
        return ResponseEntity.ok().build();
    }

}

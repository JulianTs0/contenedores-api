package backend.grupo130.tramos.controller;

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
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tramos/tramos")
@RequiredArgsConstructor
@Validated
@Slf4j
@Tag(name = "Gestión de Tramos", description = "API para la consulta y actualización de estado de los tramos (segmentos de una ruta).")
public class TramoController {

    private final TramoService tramoService;

    @Operation(summary = "Obtener un tramo por su ID",
        description = "Busca y devuelve un tramo específico utilizando su ID único, incluyendo detalles del camión y ubicaciones si están asignados.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tramo encontrado",
            content = { @Content(mediaType = "application/json",
                schema = @Schema(implementation = TramoGetByIdResponse.class)) }),
        @ApiResponse(responseCode = "404", description = "Tramo no encontrado", content = @Content),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @GetMapping("/getById/{idTramo}")
    public ResponseEntity<TramoGetByIdResponse> getById(
        @Parameter(description = "ID del tramo a buscar", required = true, example = "1")
        @NotNull(message = "{error.idTramo.notNull}")
        @Positive(message = "{error.idTramo.positive}")
        @PathVariable Long idTramo
    ) {
        log.info("Iniciando GET /api/tramos/getById/{}", idTramo);
        TramoGetByIdRequest request = new TramoGetByIdRequest(idTramo);
        return ResponseEntity.ok(this.tramoService.getById(request));
    }

    @Operation(summary = "Obtener todos los tramos",
        description = "Devuelve una lista de todos los tramos registrados en el sistema.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de tramos obtenida",
            content = { @Content(mediaType = "application/json",
                schema = @Schema(implementation = TramoGetAllResponse.class)) }),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @GetMapping("/getAll")
    public ResponseEntity<TramoGetAllResponse> getAll() {
        log.info("Iniciando GET /api/tramos/getAll");
        return ResponseEntity.ok(this.tramoService.getAll());
    }

    @Operation(summary = "Asignar un camión a un tramo",
        description = "Asigna un camión disponible (por su dominio) a un tramo que está en estado 'ESTIMADO'. " +
            "Si todos los tramos de la ruta quedan asignados, la Solicitud de traslado pasa a 'PROGRAMADO'.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Camión asignado exitosamente", content = @Content),
        @ApiResponse(responseCode = "400", description = "El tramo no está en estado 'ESTIMADO' o el camión no está disponible", content = @Content),
        @ApiResponse(responseCode = "404", description = "Tramo o Camión no encontrado", content = @Content),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @PatchMapping("/asignarCamion")
    public ResponseEntity<?> asignarCamion(
        @RequestBody @Valid TramoAsignacionCamionRequest request
    ) {
        log.info("Iniciando PATCH /api/tramos/asignarCamion para Tramo ID: {}", request.getIdTramo());
        this.tramoService.asignarCamion(request);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Obtener tramos por transportista (camión)",
        description = "Devuelve una lista de todos los tramos asignados a un camión específico, identificado por su dominio (patente).")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de tramos obtenida",
            content = { @Content(mediaType = "application/json",
                schema = @Schema(implementation = TramoGetAllResponse.class)) }),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @GetMapping("/getByTransportista/{dominio}")
    public ResponseEntity<TramoGetAllResponse> getByTransportista(
        @Parameter(description = "Dominio (patente) del camión", required = true, example = "AA123BB")
        @NotBlank(message = "{error.dominioCamion.notBlank}")
        @PathVariable String dominio
    ) {
        log.info("Iniciando GET /api/tramos/getByTransportista/{}", dominio);
        TramoGetByTransportistaRequest request = new TramoGetByTransportistaRequest(dominio);
        return ResponseEntity.ok(this.tramoService.getByTransportista(request));
    }

    @Operation(summary = "Obtener todos los tramos de una ruta",
        description = "Devuelve una lista de todos los tramos que componen una ruta específica, identificada por su ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de tramos obtenida",
            content = { @Content(mediaType = "application/json",
                schema = @Schema(implementation = TramoGetAllResponse.class)) }),
        @ApiResponse(responseCode = "404", description = "Ruta no encontrada", content = @Content),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @GetMapping("/getByRuta/{idRuta}")
    public ResponseEntity<TramoGetAllResponse> getByRuta(
        @Parameter(description = "ID de la ruta padre", required = true, example = "1")
        @NotNull(message = "{error.idRuta.notNull}")
        @Positive(message = "{error.idRuta.positive}")
        @PathVariable Long idRuta
    ) {
        log.info("Iniciando GET /api/tramos/getByRuta/{}", idRuta);
        TramoGetByRutaIdRequest request = new TramoGetByRutaIdRequest(idRuta);
        return ResponseEntity.ok(this.tramoService.getTramosDeRuta(request));
    }

    @Operation(summary = "Registrar el inicio de un tramo",
        description = "Marca un tramo como 'INICIADO'. Requiere que el tramo esté 'ASIGNADO' y que el camión que realiza la solicitud sea el asignado. " +
            "Si es el primer tramo, la Solicitud pasa a 'EN_TRANSITO'.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Inicio de tramo registrado", content = @Content),
        @ApiResponse(responseCode = "400", description = "El tramo no está 'ASIGNADO' o el tramo anterior no ha finalizado", content = @Content),
        @ApiResponse(responseCode = "403", description = "Acción no autorizada (el camión no coincide con el asignado)", content = @Content),
        @ApiResponse(responseCode = "404", description = "Tramo o Camión no encontrado", content = @Content),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @PatchMapping("/registrarInicio")
    public ResponseEntity<?> registrarInicio(
        @RequestBody @Valid TramoInicioTramoRequest request
    ) {
        log.info("Iniciando PATCH /api/tramos/registrarInicio para Tramo ID: {}", request.getIdTramo());
        this.tramoService.registrarInicioTramo(request);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Registrar el fin de un tramo",
        description = "Marca un tramo como 'FINALIZADO'. Requiere que el tramo esté 'INICIADO' y que el camión que realiza la solicitud sea el asignado. " +
            "Si es el último tramo, la Solicitud pasa a 'ENTREGADO'.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Fin de tramo registrado", content = @Content),
        @ApiResponse(responseCode = "400", description = "El tramo no está 'INICIADO'", content = @Content),
        @ApiResponse(responseCode = "403", description = "Acción no autorizada (el camión no coincide con el asignado)", content = @Content),
        @ApiResponse(responseCode = "404", description = "Tramo o Camión no encontrado", content = @Content),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @PatchMapping("/registrarFin")
    public ResponseEntity<?> registrarFin(
        @RequestBody @Valid TramoFinTramoRequest request
    ) {
        log.info("Iniciando PATCH /api/tramos/registrarFin para Tramo ID: {}", request.getIdTramo());
        this.tramoService.registrarFinTramo(request);
        return ResponseEntity.ok().build();
    }

}

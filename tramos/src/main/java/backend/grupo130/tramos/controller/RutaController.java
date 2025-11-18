package backend.grupo130.tramos.controller;

import backend.grupo130.tramos.data.models.RutaTraslado;
import backend.grupo130.tramos.dto.ruta.request.RutaAsignarSolicitudRequest;
import backend.grupo130.tramos.dto.ruta.request.RutaGetByIdRequest;
import backend.grupo130.tramos.dto.ruta.request.RutaGetOpcionesRequest;
import backend.grupo130.tramos.dto.ruta.request.RutaRegisterRequest;
import backend.grupo130.tramos.dto.ruta.response.RutaGetAllResponse;
import backend.grupo130.tramos.dto.ruta.response.RutaGetByIdResponse;
import backend.grupo130.tramos.dto.ruta.response.RutaGetOpcionesResponse;
import backend.grupo130.tramos.dto.tramo.request.TramoAsignacionCamionRequest;
import backend.grupo130.tramos.service.RutaService;
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
@RequestMapping("/api/tramos/rutas")
@RequiredArgsConstructor
@Validated
@Slf4j
@Tag(name = "Gestión de Rutas", description = "API para la creación, consulta y gestión de rutas de traslado.")
public class RutaController {

    private final RutaService rutaService;

    @Operation(summary = "Obtener una ruta por su ID",
        description = "Busca y devuelve una ruta específica utilizando su ID único, incluyendo la solicitud asociada si existe.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Ruta encontrada",
            content = { @Content(mediaType = "application/json",
                schema = @Schema(implementation = RutaGetByIdResponse.class)) }),
        @ApiResponse(responseCode = "404", description = "Ruta no encontrada", content = @Content),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @GetMapping("/getById/{idRuta}")
    public ResponseEntity<RutaGetByIdResponse> getById(
        @Parameter(description = "ID de la ruta a buscar", required = true, example = "1")
        @NotNull(message = "{error.idRuta.notNull}")
        @Positive(message = "{error.idRuta.positive}")
        @PathVariable Long idRuta
    ) {
        log.info("Iniciando GET /api/rutas/getById/{}", idRuta);
        RutaGetByIdRequest request = new RutaGetByIdRequest(idRuta);
        return ResponseEntity.ok(this.rutaService.getById(request));
    }

    @Operation(summary = "Obtener todas las rutas",
        description = "Devuelve una lista de todas las rutas de traslado registradas en el sistema.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de rutas obtenida"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @GetMapping("/getAll")
    public ResponseEntity<?> getAll() {
        log.info("Iniciando GET /api/rutas/getAll");
        return ResponseEntity.ok(this.rutaService.getAll());
    }

    @Operation(summary = "Calcular una ruta tentativa",
        description = "Recibe un ID de solicitud, una lista de IDs de ubicaciones (en orden) y cargos de gestión. " +
            "Calcula la ruta óptima (distancia, tiempo, costo estimado) y la devuelve como una opción sin confirmar, " +
            "actualizando la solicitud con los costos y tiempos estimados.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Ruta tentativa calculada exitosamente",
            content = { @Content(mediaType = "application/json",
                schema = @Schema(implementation = RutaGetOpcionesResponse.class)) }),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos", content = @Content),
        @ApiResponse(responseCode = "404", description = "Solicitud, Contenedor o Ubicación no encontrada", content = @Content),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @PostMapping("/getRutaTentativa")
    public ResponseEntity<RutaGetOpcionesResponse> getRutaTentativa(
        @RequestBody @Valid RutaGetOpcionesRequest request
    ) {
        log.info("Iniciando POST /api/rutas/getRutaTentativa para Solicitud ID: {}", request.getIdSolicitud());
        return ResponseEntity.ok(this.rutaService.getRutaTentativa(request));
    }

    @Operation(summary = "Asignar una solicitud a una ruta",
        description = "Asigna formalmente una solicitud de traslado a una ruta específica (previamente calculada) " +
            "que ya contiene sus tramos. Es el paso final para confirmar la ruta.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Solicitud asignada exitosamente", content = @Content),
        @ApiResponse(responseCode = "400", description = "La ruta no tiene tramos definidos", content = @Content),
        @ApiResponse(responseCode = "404", description = "Ruta o Solicitud no encontrada", content = @Content),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @PatchMapping("/asignarSolicitud")
    public ResponseEntity<?> asignarSolicitud(
        @RequestBody @Valid RutaAsignarSolicitudRequest request
    ) {
        log.info("Iniciando PATCH /api/rutas/asignarSolicitud para Ruta ID: {} y Solicitud ID: {}", request.getIdRuta(), request.getIdSolicitud());
        this.rutaService.asignarSolicitud(request);
        return ResponseEntity.ok().build();
    }

}

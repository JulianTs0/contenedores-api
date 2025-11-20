package backend.grupo130.tramos.controller;

import backend.grupo130.tramos.dto.ruta.request.RutaAsignarSolicitudRequest;
import backend.grupo130.tramos.dto.ruta.request.RutaGetByIdRequest;
import backend.grupo130.tramos.dto.ruta.request.RutaCrearTentativaRequest;
import backend.grupo130.tramos.dto.ruta.response.RutaGetByIdResponse;
import backend.grupo130.tramos.dto.ruta.response.RutaGetOpcionesResponse;
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

@RestController
@RequestMapping("/api/tramos/rutas")
@RequiredArgsConstructor
@Validated
@Slf4j
@Tag(name = "Gestión de Rutas", description = "API para la creación, consulta y gestión de rutas de traslado.")
public class RutaController {

    private final RutaService rutaService;

    @Operation(
        summary = "Obtener una ruta por su ID",
        description = "Busca y devuelve una ruta específica utilizando su ID único, incluyendo la solicitud asociada si existe."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Ruta encontrada exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = RutaGetByIdResponse.class))),
        @ApiResponse(responseCode = "404", description = "Ruta no encontrada", content = @Content),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @GetMapping("/getById/{idRuta}")
    public ResponseEntity<RutaGetByIdResponse> getById(
        @Parameter(description = "ID único de la ruta a buscar", required = true, example = "1")
        @NotNull(message = "{error.idRuta.notNull}")
        @Positive(message = "{error.idRuta.positive}")
        @PathVariable Long idRuta
    ) {
        log.info("Iniciando GET /api/rutas/getById/{}", idRuta);
        RutaGetByIdRequest request = new RutaGetByIdRequest(idRuta);
        return ResponseEntity.ok(this.rutaService.getById(request));
    }

    @Operation(
        summary = "Obtener todas las rutas",
        description = "Devuelve una lista completa de todas las rutas de traslado registradas en el sistema."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de rutas obtenida exitosamente", content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @GetMapping("/getAll")
    public ResponseEntity<?> getAll() {
        log.info("Iniciando GET /api/rutas/getAll");
        return ResponseEntity.ok(this.rutaService.getAll());
    }

    @Operation(
        summary = "Calcular una ruta tentativa",
        description = "Calcula la ruta óptima (distancia, tiempo, costo) basada en una solicitud y una lista de ubicaciones. Devuelve opciones sin confirmar."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Ruta tentativa calculada exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = RutaGetOpcionesResponse.class))),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos (ej: faltan ubicaciones)", content = @Content),
        @ApiResponse(responseCode = "404", description = "Solicitud o Ubicación no encontrada", content = @Content),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @PostMapping("/crearRutaTentativa")
    public ResponseEntity<RutaGetOpcionesResponse> crearRutaTentativa(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos para el cálculo de la ruta (Solicitud + Ubicaciones)", required = true)
        @RequestBody @Valid RutaCrearTentativaRequest request
    ) {
        log.info("Iniciando POST /api/rutas/getRutaTentativa para Solicitud ID: {}", request.getIdSolicitud());
        return ResponseEntity.ok(this.rutaService.getRutaTentativa(request));
    }

}

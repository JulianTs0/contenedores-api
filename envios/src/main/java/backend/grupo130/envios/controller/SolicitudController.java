package backend.grupo130.envios.controller;

import backend.grupo130.envios.dto.solicitud.SolicitudMapperDto;
import backend.grupo130.envios.dto.solicitud.request.*;
import backend.grupo130.envios.dto.solicitud.response.SolicitudCambioDeEstadoResponse;
import backend.grupo130.envios.dto.solicitud.response.SolicitudEditResponse;
import backend.grupo130.envios.dto.solicitud.response.SolicitudGetAllResponse;
import backend.grupo130.envios.dto.solicitud.response.SolicitudGetByIdResponse;
import backend.grupo130.envios.service.SolicitudService;
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
@RequestMapping("/api/envios/solicitud")
@RequiredArgsConstructor
@Slf4j
@Validated
@Tag(name = "Gestión de Solicitudes de Envío", description = "API para la creación, seguimiento, confirmación y edición de solicitudes de envío.")
public class SolicitudController {

    private final SolicitudService solicitudService;

    @Operation(
            summary = "Obtener solicitud por ID",
            description = "Busca y devuelve el detalle completo de una solicitud de envío específica."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Solicitud encontrada exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SolicitudGetByIdResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "ID inválido (debe ser positivo)",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Solicitud no encontrada",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<SolicitudGetByIdResponse> getById(
            @Parameter(description = "ID único de la solicitud a consultar", required = true, example = "1")
            @NotNull(message = "{error.idSolicitud.notNull}")
            @Positive(message = "{error.idSolicitud.positive}")
            @PathVariable Long id
    ) {
        log.info("Recibida solicitud GET en /api/solicitudes/{}", id);
        SolicitudGetByIdRequest request = new SolicitudGetByIdRequest(id);
        return ResponseEntity.ok(this.solicitudService.getById(request));
    }

    @Operation(
            summary = "Obtener todas las solicitudes",
            description = "Devuelve un listado completo de todas las solicitudes de envío registradas en el sistema."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Listado recuperado exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SolicitudGetAllResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content
            )
    })
    @GetMapping("/")
    public ResponseEntity<SolicitudGetAllResponse> getAll() {
        log.info("Recibida solicitud GET en /api/solicitudes/");
        return ResponseEntity.ok(this.solicitudService.getAll());
    }

    @Operation(
            summary = "Registrar una nueva solicitud",
            description = "Crea una nueva solicitud de envío inicial asociada a un cliente."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Solicitud creada exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SolicitudGetByIdResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos inválidos (ej: valores negativos)",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Cliente no encontrado",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content
            )
    })
    @PostMapping("/")
    public ResponseEntity<SolicitudGetByIdResponse> register(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos iniciales de la solicitud", required = true)
            @RequestBody @Valid SolicitudRegisterRequest request) {
        log.info("Recibida solicitud POST en /api/solicitudes/ para cliente ID: {}", request.getIdCliente());
        return ResponseEntity.status(HttpStatus.CREATED).body(this.solicitudService.register(request));
    }

    @Operation(
            summary = "Editar detalles de solicitud",
            description = "Actualiza información logística de la solicitud (fechas, costos, tiempos, tarifas)."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Solicitud editada exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SolicitudEditResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos inválidos",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Solicitud no encontrada",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<SolicitudEditResponse> edit(
            @Parameter(description = "ID único de la solicitud a editar", required = true, example = "1")
            @NotNull(message = "{error.idSolicitud.notNull}")
            @Positive(message = "{error.idSolicitud.positive}")
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos logísticos a actualizar", required = true)
            @RequestBody @Valid SolicitudEditRequest body) {
        log.info("Recibida solicitud PUT en /api/solicitudes/{}. Solicitud ID: {}", id, id);
        SolicitudEditRequest request = SolicitudMapperDto.toSolicitudEditRequest(id, body);
        return ResponseEntity.ok(this.solicitudService.edit(request));
    }

    @Operation(
            summary = "Cambiar estado de solicitud",
            description = "Permite actualizar el estado de una solicitud siguiendo el flujo de negocio."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Estado actualizado correctamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SolicitudCambioDeEstadoResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Estado inválido o transición no permitida",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Solicitud no encontrada",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content
            )
    })
    @PutMapping("/{id}/estado")
    public ResponseEntity<SolicitudCambioDeEstadoResponse> cambioDeEstado(
            @Parameter(description = "ID único de la solicitud a modificar", required = true, example = "1")
            @NotNull(message = "{error.idSolicitud.notNull}")
            @Positive(message = "{error.idSolicitud.positive}")
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Nuevo estado y motivo del cambio", required = true)
            @RequestBody @Valid SolicitudCambioDeEstadoRequest body) {
        log.info("Recibida solicitud PUT en /{id}/estado. Solicitud ID: {}, Nuevo Estado: {}", id, body.getNuevoEstado());
        SolicitudCambioDeEstadoRequest request = SolicitudMapperDto.toSolicitudCambioDeEstadoRequest(id, body);
        return ResponseEntity.ok(this.solicitudService.cambioDeEstado(request));
    }

    @Operation(
            summary = "Confirmar ruta de envío",
            description = "Confirma la ruta asignada a una solicitud."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Ruta confirmada exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SolicitudCambioDeEstadoResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "La solicitud no está en un estado válido para ser confirmada",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Solicitud no encontrada",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content
            )
    })
    @PatchMapping("/{id}/confirmar")
    public ResponseEntity<SolicitudCambioDeEstadoResponse> confirmarSolicitud(
            @Parameter(description = "ID de la solicitud a confirmar", required = true, example = "1")
            @NotNull(message = "{error.idSolicitud.notNull}")
            @Positive(message = "{error.idSolicitud.positive}")
            @PathVariable Long id
    ) {
        log.info("Recibida solicitud PATCH en /{id}/confirmar. Solicitud ID: {}", id);
        SolicitudConfirmarRuta request = new SolicitudConfirmarRuta(id);
        return ResponseEntity.ok(this.solicitudService.confirmarSolicitud(request));
    }
}

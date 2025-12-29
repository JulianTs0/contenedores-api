package backend.grupo130.envios.controller;

import backend.grupo130.envios.dto.seguimiento.SeguimientoMapperDto;
import backend.grupo130.envios.dto.seguimiento.request.SeguimientoGetByIdRequest;
import backend.grupo130.envios.dto.seguimiento.request.SeguimientoRegisterRequest;
import backend.grupo130.envios.dto.seguimiento.response.SeguimientoGetAllResponse;
import backend.grupo130.envios.dto.seguimiento.response.SeguimientoGetByIdResponse;
import backend.grupo130.envios.service.SeguimientoService;
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
import org.springframework.web.bind.annotation.*;

import org.springframework.validation.annotation.Validated;

@RestController
@RequestMapping("/api/envios/seguimiento")
@RequiredArgsConstructor
@Slf4j
@Validated
@Tag(name = "Gestión de Seguimientos", description = "API para consultar y registrar hitos en el historial de seguimiento de los envíos.")
public class SeguimientoController {

    private final SeguimientoService seguimientoService;

    @Operation(
            summary = "Obtener seguimiento por ID",
            description = "Recupera el detalle de un registro de seguimiento específico mediante su ID único."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Seguimiento encontrado exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SeguimientoGetByIdResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "ID inválido (debe ser positivo)",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Registro de seguimiento no encontrado",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<SeguimientoGetByIdResponse> getById(
        @Parameter(description = "ID único del registro de seguimiento", required = true, example = "10")
        @NotNull(message = "{error.idSeguimiento.notNull}")
        @Positive(message = "{error.idSeguimiento.positive}")
        @PathVariable Long id
    ) {
        log.info("Recibida solicitud GET en /api/seguimientos/{}", id);
        SeguimientoGetByIdRequest request = new SeguimientoGetByIdRequest(id);
        return ResponseEntity.ok(this.seguimientoService.getById(request));
    }

    @Operation(
            summary = "Obtener todos los seguimientos",
            description = "Devuelve el historial completo de todos los seguimientos registrados en el sistema."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de seguimientos recuperada exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SeguimientoGetAllResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content
            )
    })
    @GetMapping("/")
    public ResponseEntity<SeguimientoGetAllResponse> getAll() {
        log.info("Recibida solicitud GET en /api/seguimientos/");
        return ResponseEntity.ok(this.seguimientoService.getAll());
    }

    @Operation(
            summary = "Registrar un nuevo hito de seguimiento",
            description = "Agrega un nuevo evento al historial de un envío (ej: 'En tránsito', 'Entregado'). Requiere el ID de la solicitud asociada."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Seguimiento registrado exitosamente",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos inválidos o estado vacío",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Solicitud de envío no encontrada",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content
            )
    })
    @PostMapping("/")
    public ResponseEntity<Void> register(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos del nuevo evento de seguimiento", required = true)
        @RequestBody @Valid SeguimientoRegisterRequest request
    ) {
        log.info("Recibida solicitud POST en /api/seguimientos/ para Solicitud ID: {}", request.getIdSolicitud());
        this.seguimientoService.register(request);
        return ResponseEntity.ok().build();
    }
}

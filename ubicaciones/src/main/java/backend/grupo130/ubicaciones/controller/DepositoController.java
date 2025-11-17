package backend.grupo130.ubicaciones.controller;

import backend.grupo130.ubicaciones.dto.deposito.request.DepositoDeleteRequest;
import backend.grupo130.ubicaciones.dto.deposito.request.DepositoEditRequest;
import backend.grupo130.ubicaciones.dto.deposito.request.DepositoGetByIdRequest;
import backend.grupo130.ubicaciones.dto.deposito.request.DepositoRegisterRequest;
import backend.grupo130.ubicaciones.dto.deposito.response.DepositoEditResponse;
import backend.grupo130.ubicaciones.dto.deposito.response.DepositoGetAllResponse;
import backend.grupo130.ubicaciones.dto.deposito.response.DepositoGetByIdResponse;
import backend.grupo130.ubicaciones.service.DepositoService;
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
@RequestMapping("/api/ubicaciones/depositos")
@RequiredArgsConstructor
@Validated
@Slf4j // <--- Añadido Logger
@Tag(name = "Gestión de Depósitos", description = "Endpoints para crear, leer, actualizar y eliminar Depósitos.")
public class DepositoController {

    private final DepositoService depositoService;

    @Operation(summary = "Obtener un depósito por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Depósito encontrado",
            content = { @Content(mediaType = "application/json",
                schema = @Schema(implementation = DepositoGetByIdResponse.class)) }),
        @ApiResponse(responseCode = "400", description = "ID inválido (no es positivo)", content = @Content),
        @ApiResponse(responseCode = "404", description = "Depósito no encontrado", content = @Content),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @GetMapping("/getById/{idDeposito}")
    public ResponseEntity<DepositoGetByIdResponse> getById(
        @Parameter(description = "ID del depósito a buscar", required = true, example = "1")
        @NotNull(message = "{error.idDeposito.notNull}")
        @Positive(message = "{error.idDeposito.positive}")
        @PathVariable Long idDeposito
    ) {
        log.info("Iniciando getById para Deposito con ID: {}", idDeposito);
        DepositoGetByIdRequest request = new DepositoGetByIdRequest(idDeposito);
        DepositoGetByIdResponse response = this.depositoService.getById(request);
        log.info("Finalizado getById para Deposito con ID: {}", idDeposito);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Obtener todos los depósitos")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Listado de depósitos",
            content = { @Content(mediaType = "application/json",
                schema = @Schema(implementation = DepositoGetAllResponse.class)) }),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @GetMapping("/getAll")
    public ResponseEntity<DepositoGetAllResponse> getAll() {
        log.info("Iniciando getAll para Depositos");
        DepositoGetAllResponse response = this.depositoService.getAll();
        log.info("Finalizado getAll para Depositos, total encontrados: {}", response.getDepositos().size());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Registrar un nuevo depósito y asignarlo a una ubicación")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Depósito registrado exitosamente", content = @Content),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos o la ubicación ya tiene un depósito", content = @Content),
        @ApiResponse(responseCode = "404", description = "Ubicación no encontrada", content = @Content),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @PostMapping("/register")
    public ResponseEntity<?> register(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos del nuevo depósito", required = true,
            content = @Content(schema = @Schema(implementation = DepositoRegisterRequest.class)))
        @RequestBody @Valid DepositoRegisterRequest request
    ) {
        log.info("Iniciando register para nuevo Deposito en Ubicacion ID: {}", request.getIdUbicacion());
        this.depositoService.register(request);
        log.info("Finalizado register para nuevo Deposito en Ubicacion ID: {}", request.getIdUbicacion());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Editar un depósito existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Depósito editado exitosamente",
            content = { @Content(mediaType = "application/json",
                schema = @Schema(implementation = DepositoEditResponse.class)) }),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos", content = @Content),
        @ApiResponse(responseCode = "404", description = "Depósito no encontrado", content = @Content),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @PatchMapping("/edit")
    public ResponseEntity<DepositoEditResponse> edit(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos a modificar del depósito. El ID es obligatorio.", required = true,
            content = @Content(schema = @Schema(implementation = DepositoEditRequest.class)))
        @RequestBody @Valid DepositoEditRequest request
    ) {
        log.info("Iniciando edit para Deposito con ID: {}", request.getIdDeposito());
        DepositoEditResponse response = this.depositoService.edit(request);
        log.info("Finalizado edit para Deposito con ID: {}", request.getIdDeposito());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Eliminar un depósito por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Depósito eliminado exitosamente", content = @Content),
        @ApiResponse(responseCode = "400", description = "ID inválido (no es positivo)", content = @Content),
        @ApiResponse(responseCode = "404", description = "Depósito no encontrado", content = @Content),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(
        @Parameter(description = "ID del depósito a eliminar", required = true, example = "1")
        @NotNull(message = "{error.idDeposito.notNull}")
        @Positive(message = "{error.idDeposito.positive}")
        @PathVariable Long id
    ) {
        log.info("Iniciando delete para Deposito con ID: {}", id);
        DepositoDeleteRequest request = new DepositoDeleteRequest(id);
        this.depositoService.delete(request);
        log.info("Finalizado delete para Deposito con ID: {}", id);
        return ResponseEntity.ok().build();
    }

}

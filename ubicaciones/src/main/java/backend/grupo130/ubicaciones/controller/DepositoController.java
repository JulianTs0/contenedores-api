
package backend.grupo130.ubicaciones.controller;

import backend.grupo130.ubicaciones.dto.deposito.DepositoMapperDto;
import backend.grupo130.ubicaciones.dto.deposito.request.DepositoEditRequest;
import backend.grupo130.ubicaciones.dto.deposito.request.DepositoGetByIdRequest;
import backend.grupo130.ubicaciones.dto.deposito.request.DepositoRegisterRequest;
import backend.grupo130.ubicaciones.dto.deposito.response.DepositoEditResponse;
import backend.grupo130.ubicaciones.dto.deposito.response.DepositoGetAllResponse;
import backend.grupo130.ubicaciones.dto.deposito.response.DepositoGetByIdResponse;
import backend.grupo130.ubicaciones.dto.deposito.response.DepositoRegisterResponse;
import backend.grupo130.ubicaciones.service.DepositoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

import java.util.List;

@RestController
@RequestMapping("/api/ubicaciones/depositos")
@RequiredArgsConstructor
@Validated
@Slf4j
@Tag(name = "Depositos", description = "Endpoints para la gestión de Depositos.")
public class DepositoController {

    private final DepositoService depositoService;

    @Operation(
            summary = "Obtener un Deposito por su ID",
            description = "Devuelve un único Deposito.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Deposito encontrado.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = DepositoGetByIdResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Deposito no encontrado."
                    )
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<DepositoGetByIdResponse> getById(
            @PathVariable
            @NotNull(message = "{error.idDeposito.notNull}")
            @Positive(message = "{error.idDeposito.positive}")
            Long id
    ) {
        DepositoGetByIdRequest request = new DepositoGetByIdRequest(id);
        DepositoGetByIdResponse response = this.depositoService.getById(request);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Obtener todos los Depositos",
            description = "Devuelve una lista de todos los Depositos.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista de Depositos.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            implementation = DepositoGetAllResponse.class
                                    )
                            )
                    )
            }
    )
    @GetMapping("/")
    public ResponseEntity<DepositoGetAllResponse> getAll() {
        DepositoGetAllResponse response = this.depositoService.getAll();
        return ResponseEntity.ok(response);
    }


    @Operation(
            summary = "Registrar un nuevo Deposito y asignarlo a una ubicación",
            description = "Crea un nuevo Deposito y lo devuelve.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos del nuevo Deposito.",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = DepositoRegisterRequest.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Deposito creado exitosamente.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = DepositoRegisterResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Solicitud inválida."
                    )
            }
    )
    @PostMapping("/")
    public ResponseEntity<DepositoRegisterResponse> register(@RequestBody @Valid DepositoRegisterRequest request) {
        DepositoRegisterResponse response = this.depositoService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
            summary = "Actualizar un Deposito existente",
            description = "Actualiza un Deposito existente y lo devuelve.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos para actualizar el Deposito.",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = DepositoEditRequest.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Deposito actualizado exitosamente.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = DepositoEditResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Deposito no encontrado."
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Solicitud inválida."
                    )
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<DepositoEditResponse> edit(
            @PathVariable
            @NotNull(message = "{error.idDeposito.notNull}")
            @Positive(message = "{error.idDeposito.positive}")
            Long id,
            @RequestBody @Valid DepositoEditRequest request
    ) {
        DepositoEditRequest requestWithId = DepositoMapperDto.toRequestPatchEdit(id, request);
        DepositoEditResponse response = this.depositoService.edit(requestWithId);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Eliminar un Deposito por su ID",
            description = "Elimina un Deposito existente.",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Deposito eliminado exitosamente."
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Deposito no encontrado."
                    )
            }
    )
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> delete(
            @PathVariable
            @NotNull(message = "{error.idDeposito.notNull}")
            @Positive(message = "{error.idDeposito.positive}")
            Long id
    ) {
        this.depositoService.delete(id);
        return ResponseEntity.noContent().build();
    }

}

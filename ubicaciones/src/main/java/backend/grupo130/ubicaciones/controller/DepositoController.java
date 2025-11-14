package backend.grupo130.ubicaciones.controller;

import backend.grupo130.ubicaciones.data.models.Deposito;
import backend.grupo130.ubicaciones.dto.deposito.request.DepositoEditRequest;
import backend.grupo130.ubicaciones.dto.deposito.request.DepositoGetByIdRequest;
import backend.grupo130.ubicaciones.dto.deposito.request.DepositoRegisterRequest;
import backend.grupo130.ubicaciones.dto.deposito.response.DepositoEditResponse;
import backend.grupo130.ubicaciones.dto.deposito.response.DepositoGetAllResponse;
import backend.grupo130.ubicaciones.dto.deposito.response.DepositoGetByIdResponse;
import backend.grupo130.ubicaciones.dto.ubicaciones.response.UbicacionGetByIdResponse;
import backend.grupo130.ubicaciones.service.DepositoService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/depositos")
@RequiredArgsConstructor
@Validated
public class DepositoController {

    private final DepositoService depositoService;

    @GetMapping("/getById/{id}")
    public ResponseEntity<DepositoGetByIdResponse> getById(
        @NotNull(message = "El ID de deposito no puede ser nulo")
        @Positive(message = "El ID del deposito debe ser un número positivo")
        @PathVariable Integer id
    ) {

        DepositoGetByIdRequest request = new DepositoGetByIdRequest(id);

        Deposito deposito = this.depositoService.getById(request);

        return ResponseEntity.ok(this.toResponseGet(deposito));
    }

    @GetMapping("/getAll")
    public ResponseEntity<?> getAll() {

        List<Deposito> depositos = this.depositoService.getAll();

        return ResponseEntity.ok(this.toResponseGet(depositos));
    }

    @PostMapping("/register")
    public ResponseEntity<DepositoRegisterRequest> register(
        @RequestBody @Valid DepositoRegisterRequest request
    ) {

        this.depositoService.register(request);

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/edit")
    public ResponseEntity<DepositoEditResponse> edit(
        @RequestBody @Valid DepositoEditRequest request
    ) {

        Deposito deposito = this.depositoService.edit(request);

        return ResponseEntity.ok(this.toResponsePatch(deposito));
    }

    // Respuestas

    private DepositoGetByIdResponse toResponseGet(Deposito deposito) {
        return new DepositoGetByIdResponse(
            deposito.getIdDeposito(),
            deposito.getNombre(),
            deposito.getCostoEstadiaDiario(),
            deposito.getUbicacion().getIdUbicacion()
        );
    }

    private DepositoGetAllResponse toResponseGet(List<Deposito> depositos) {
        return new DepositoGetAllResponse(
            depositos.stream().map(this::toResponseGet).toList()
        );
    }

    private DepositoEditResponse toResponsePatch(Deposito deposito) {
        return new DepositoEditResponse(
            deposito.getIdDeposito(),
            deposito.getNombre(),
            deposito.getCostoEstadiaDiario(),
            deposito.getUbicacion().getIdUbicacion()
        );
    }

}


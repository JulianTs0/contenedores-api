package backend.grupo130.ubicaciones.controller;

import backend.grupo130.ubicaciones.dto.deposito.request.DepositoDeleteRequest;
import backend.grupo130.ubicaciones.dto.deposito.request.DepositoEditRequest;
import backend.grupo130.ubicaciones.dto.deposito.request.DepositoGetByIdRequest;
import backend.grupo130.ubicaciones.dto.deposito.request.DepositoRegisterRequest;
import backend.grupo130.ubicaciones.dto.deposito.response.DepositoEditResponse;
import backend.grupo130.ubicaciones.dto.deposito.response.DepositoGetAllResponse;
import backend.grupo130.ubicaciones.dto.deposito.response.DepositoGetByIdResponse;
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
@RequestMapping("/api/ubicaciones/depositos")
@RequiredArgsConstructor
@Validated
public class DepositoController {

    private final DepositoService depositoService;

    @GetMapping("/getById/{idDeposito}")
    public ResponseEntity<DepositoGetByIdResponse> getById(
        @NotNull(message = "{error.idDeposito.notNull}")
        @Positive(message = "{error.idDeposito.positive}")
        @PathVariable Long idDeposito
    ) {

        DepositoGetByIdRequest request = new DepositoGetByIdRequest(idDeposito);

        return ResponseEntity.ok(this.depositoService.getById(request));
    }

    @GetMapping("/getAll")
    public ResponseEntity<DepositoGetAllResponse> getAll() {

        return ResponseEntity.ok(this.depositoService.getAll());
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(
        @RequestBody @Valid DepositoRegisterRequest request
    ) {

        this.depositoService.register(request);

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/edit")
    public ResponseEntity<DepositoEditResponse> edit(
        @RequestBody @Valid DepositoEditRequest request
    ) {

        return ResponseEntity.ok(this.depositoService.edit(request));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(
        @NotNull(message = "{error.idDeposito.notNull}")
        @Positive(message = "{error.idDeposito.positive}")
        @PathVariable Long id
    ) {

        DepositoDeleteRequest request = new DepositoDeleteRequest(id);

        this.depositoService.delete(request);

        return ResponseEntity.ok().build();
    }

}


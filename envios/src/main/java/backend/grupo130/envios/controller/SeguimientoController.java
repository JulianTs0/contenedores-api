package backend.grupo130.envios.controller;

import backend.grupo130.envios.dto.seguimiento.request.SeguimientoGetByIdRequest;
import backend.grupo130.envios.dto.seguimiento.request.SeguimientoRegisterRequest;
import backend.grupo130.envios.dto.seguimiento.response.SeguimientoGetAllResponse;
import backend.grupo130.envios.dto.seguimiento.response.SeguimientoGetByIdResponse;
import backend.grupo130.envios.service.SeguimientoService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/envios/seguimiento")
@RequiredArgsConstructor
public class SeguimientoController {

    private final SeguimientoService seguimientoService;

    @GetMapping("/getById/{id}")
    public ResponseEntity<SeguimientoGetByIdResponse> getById(
            @NotNull(message = "{error.idSeguimiento.notNull}")
            @Positive(message = "{error.idSeguimiento.positive}")
            @PathVariable Long id
    ) {
        SeguimientoGetByIdRequest request = new SeguimientoGetByIdRequest(id);
        return ResponseEntity.ok(this.seguimientoService.getById(request));
    }

    @GetMapping("/getAll")
    public ResponseEntity<SeguimientoGetAllResponse> getAll() {
        return ResponseEntity.ok(this.seguimientoService.getAll());
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(
            @RequestBody @Valid SeguimientoRegisterRequest request
    ) {
        this.seguimientoService.register(request);
        return ResponseEntity.ok().build();
    }
}

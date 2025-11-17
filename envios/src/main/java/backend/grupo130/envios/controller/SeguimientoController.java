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
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/envios/seguimiento")
@RequiredArgsConstructor
@Slf4j
public class SeguimientoController {

    private final SeguimientoService seguimientoService;

    @GetMapping("/getById/{id}")
    public ResponseEntity<SeguimientoGetByIdResponse> getById(
        @NotNull(message = "{error.idSeguimiento.notNull}")
        @Positive(message = "{error.idSeguimiento.positive}")
        @PathVariable Long id
    ) {
        // LOG Nivel INFO: Evento importante de flujo
        log.info("Recibida solicitud GET en /seguimiento/getById/{}", id);
        SeguimientoGetByIdRequest request = new SeguimientoGetByIdRequest(id);
        return ResponseEntity.ok(this.seguimientoService.getById(request));
    }

    @GetMapping("/getAll")
    public ResponseEntity<SeguimientoGetAllResponse> getAll() {
        log.info("Recibida solicitud GET en /seguimiento/getAll");
        return ResponseEntity.ok(this.seguimientoService.getAll());
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(
        @RequestBody @Valid SeguimientoRegisterRequest request
    ) {
        log.info("Recibida solicitud POST en /seguimiento/register para Solicitud ID: {}", request.getIdSolicitud());
        this.seguimientoService.register(request);
        return ResponseEntity.ok().build();
    }
}

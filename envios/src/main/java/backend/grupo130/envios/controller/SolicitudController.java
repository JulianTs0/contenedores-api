package backend.grupo130.envios.controller;

import backend.grupo130.envios.dto.solicitud.request.*;
import backend.grupo130.envios.dto.solicitud.response.SolicitudCambioDeEstadoResponse;
import backend.grupo130.envios.dto.solicitud.response.SolicitudEditResponse;
import backend.grupo130.envios.dto.solicitud.response.SolicitudGetAllResponse;
import backend.grupo130.envios.dto.solicitud.response.SolicitudGetByIdResponse;
import backend.grupo130.envios.service.SolicitudService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import org.springframework.validation.annotation.Validated;

@RestController
@RequestMapping("/api/envios/solicitud")
@RequiredArgsConstructor
@Slf4j
@Validated
public class SolicitudController {

    private final SolicitudService solicitudService;

    @GetMapping("/getById/{id}")
    public ResponseEntity<SolicitudGetByIdResponse> getById(
        @NotNull(message = "{error.idSolicitud.notNull}")
        @Positive(message = "{error.idSolicitud.positive}")
        @PathVariable Long id
    ) {
        log.info("Recibida solicitud GET en /getById/{}", id);
        SolicitudGetByIdRequest request = new SolicitudGetByIdRequest(id);
        return ResponseEntity.ok(this.solicitudService.getById(request));
    }

    @GetMapping("/getAll")
    public ResponseEntity<SolicitudGetAllResponse> getAll() {
        log.info("Recibida solicitud GET en /getAll");
        return ResponseEntity.ok(this.solicitudService.getAll());
    }

    @PostMapping("/register")
    public ResponseEntity<SolicitudGetByIdResponse> register(
        @RequestBody @Valid SolicitudRegisterRequest request
    ) {
        log.info("Recibida solicitud POST en /register para cliente ID: {}", request.getIdCliente());
        return ResponseEntity.status(HttpStatus.CREATED).body(this.solicitudService.register(request));
    }

    @PutMapping("/cambioDeEstado")
    public ResponseEntity<SolicitudCambioDeEstadoResponse> cambioDeEstado(
        @RequestBody @Valid SolicitudCambioDeEstadoRequest request
    ) {
        log.info("Recibida solicitud POST en /cambioDeEstado. Solicitud ID: {}, Nuevo Estado: {}", request.getIdSolicitud(), request.getNuevoEstado());
        return ResponseEntity.ok(this.solicitudService.cambioDeEstado(request));
    }

    @PatchMapping("/confirmarSolicitud")
    public ResponseEntity<SolicitudCambioDeEstadoResponse> confirmarSolicitud(
        @RequestBody @Valid SolicitudConfirmarRuta request
    ) {
        return ResponseEntity.ok(this.solicitudService.confirmarSolicitud(request));
    }

    @PutMapping("/edit")
    public ResponseEntity<SolicitudEditResponse> edit(
        @RequestBody @Valid SolicitudEditRequest request
    ) {
        log.info("Recibida solicitud POST en /edit. Solicitud ID: {}", request.getIdSolicitud());
        return ResponseEntity.ok(this.solicitudService.edit(request));
    }
}

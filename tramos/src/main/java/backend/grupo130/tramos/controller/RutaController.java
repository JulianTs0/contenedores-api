package backend.grupo130.tramos.controller;

import backend.grupo130.tramos.data.models.RutaTraslado;
import backend.grupo130.tramos.dto.ruta.request.RutaAsignarSolicitudRequest;
import backend.grupo130.tramos.dto.ruta.request.RutaGetByIdRequest;
import backend.grupo130.tramos.dto.ruta.request.RutaGetOpcionesRequest;
import backend.grupo130.tramos.dto.ruta.request.RutaRegisterRequest;
import backend.grupo130.tramos.dto.ruta.response.RutaGetAllResponse;
import backend.grupo130.tramos.dto.ruta.response.RutaGetByIdResponse;
import backend.grupo130.tramos.dto.ruta.response.RutaGetOpcionesResponse;
import backend.grupo130.tramos.dto.tramo.request.TramoAsignacionCamionRequest;
import backend.grupo130.tramos.service.RutaService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rutas")
@RequiredArgsConstructor
@Validated
public class RutaController {

    private final RutaService rutaService;

    @GetMapping("/getById/{idRuta}")
    public ResponseEntity<RutaGetByIdResponse> getById(
        @NotNull(message = "{error.idRuta.notNull}")
        @Positive(message = "{error.idRuta.positive}")
        @PathVariable Long idRuta
    ) {

        RutaGetByIdRequest request = new RutaGetByIdRequest(idRuta);

        return ResponseEntity.ok(this.rutaService.getById(request));
    }

    @GetMapping("/getAll")
    public ResponseEntity<?> getAll() {

        return ResponseEntity.ok(this.rutaService.getAll());
    }

    @PostMapping("/getRutaTentativa")
    public ResponseEntity<RutaGetOpcionesResponse> register(
        @RequestBody @Valid RutaGetOpcionesRequest request
    ) {

        return ResponseEntity.ok(this.rutaService.getRutaTentativa(request));
    }

    @PatchMapping("/register")
    public ResponseEntity<?> register(
        @RequestBody @Valid RutaRegisterRequest request
    ) {

        this.rutaService.register(request);

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/asignarSolicitud")
    public ResponseEntity<?> asignarSolicitud(
        @RequestBody @Valid RutaAsignarSolicitudRequest request
    ) {

        this.rutaService.asignarSolicitud(request);

        return ResponseEntity.ok().build();
    }

}

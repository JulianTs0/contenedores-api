package backend.grupo130.tramos.controller;

import backend.grupo130.tramos.data.models.RutaTraslado;
import backend.grupo130.tramos.dto.ruta.request.RutaGetByIdRequest;
import backend.grupo130.tramos.dto.ruta.request.RutaRegisterRequest;
import backend.grupo130.tramos.dto.ruta.response.RutaGetAllResponse;
import backend.grupo130.tramos.dto.ruta.response.RutaGetByIdResponse;
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

    @GetMapping("/getById/{id}")
    public ResponseEntity<RutaGetByIdResponse> getById(
        @NotNull(message = "El ID de la ruta no puede ser nulo")
        @Positive(message = "El ID de la ruta debe ser un número positivo")
        @PathVariable Integer id
    ) {

        RutaGetByIdRequest request = new RutaGetByIdRequest(id);

        RutaTraslado ruta = this.rutaService.getById(request);

        return ResponseEntity.ok(this.toResponseGet(ruta));
    }

    @GetMapping("/getAll")
    public ResponseEntity<?> getAll() {

        List<RutaTraslado> rutas = this.rutaService.getAll();

        return ResponseEntity.ok(this.toResponseGet(rutas));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(
        @RequestBody @Valid RutaRegisterRequest request
    ) {

        this.rutaService.register(request);

        return ResponseEntity.ok().build();
    }

    // Respuestas

    private RutaGetByIdResponse toResponseGet(RutaTraslado ruta) {
        return new RutaGetByIdResponse(
            ruta.getIdRuta(),
            ruta.getCantidadTramos(),
            ruta.getCantidadDepositos(),
            ruta.getCargosGestionFijo(),
            ruta.getSolicitud()
        );
    }

    private RutaGetAllResponse toResponseGet(List<RutaTraslado> rutas) {
        return new RutaGetAllResponse(
            rutas
        );
    }

}

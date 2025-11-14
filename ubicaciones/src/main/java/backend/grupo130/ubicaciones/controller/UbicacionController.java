
package backend.grupo130.ubicaciones.controller;

import backend.grupo130.ubicaciones.data.models.Ubicacion;
import backend.grupo130.ubicaciones.dto.ubicaciones.request.UbicacionEditRequest;
import backend.grupo130.ubicaciones.dto.ubicaciones.request.UbicacionGetByIdRequest;
import backend.grupo130.ubicaciones.dto.ubicaciones.request.UbicacionRegisterRequest;
import backend.grupo130.ubicaciones.dto.ubicaciones.response.UbicacionEditResponse;
import backend.grupo130.ubicaciones.dto.ubicaciones.response.UbicacionGetAllResponse;
import backend.grupo130.ubicaciones.dto.ubicaciones.response.UbicacionGetByIdResponse;
import backend.grupo130.ubicaciones.service.UbicacionService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ubicaciones")
@RequiredArgsConstructor
@Validated
public class UbicacionController {

    private final UbicacionService ubicacionService;

    @GetMapping("/getById/{id}")
    public ResponseEntity<UbicacionGetByIdResponse> getById(
        @NotNull(message = "El ID de ubicacion no puede ser nulo")
        @Positive(message = "El ID del ubicacion debe ser un número positivo")
        @PathVariable Integer id
    ) {

        UbicacionGetByIdRequest request = new UbicacionGetByIdRequest(id);

        Ubicacion ubicacion = this.ubicacionService.getById(request);

        return ResponseEntity.ok(this.toResponseGet(ubicacion));
    }

    @GetMapping("/getAll")
    public ResponseEntity<?> getAll() {

        List<Ubicacion> ubicaciones = this.ubicacionService.getAll();

        return ResponseEntity.ok(this.toResponseGet(ubicaciones));
    }

    @PostMapping("/register")
    public ResponseEntity<UbicacionRegisterRequest> register(
        @RequestBody @Valid UbicacionRegisterRequest request
    ) {

        this.ubicacionService.register(request);

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/edit")
    public ResponseEntity<UbicacionEditResponse> edit(
        @RequestBody @Valid UbicacionEditRequest request
    ) {

        Ubicacion ubicacion = this.ubicacionService.edit(request);

        return ResponseEntity.ok(this.toResponsePatch(ubicacion));
    }

    // Respuestas

    private UbicacionGetByIdResponse toResponseGet(Ubicacion ubicacion) {
        return new UbicacionGetByIdResponse(
            ubicacion.getIdUbicacion(),
            ubicacion.getDireccionTextual(),
            ubicacion.getLatitud(),
            ubicacion.getLongitud()
        );
    }

    private UbicacionGetAllResponse toResponseGet(List<Ubicacion> ubicaciones) {
        return new UbicacionGetAllResponse(
            ubicaciones
        );
    }

    private UbicacionEditResponse toResponsePatch(Ubicacion ubicacion) {
        return new UbicacionEditResponse(
            ubicacion.getIdUbicacion(),
            ubicacion.getDireccionTextual(),
            ubicacion.getLatitud(),
            ubicacion.getLongitud()
        );
    }

}

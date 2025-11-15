
package backend.grupo130.ubicaciones.controller;

import backend.grupo130.ubicaciones.data.models.Ubicacion;
import backend.grupo130.ubicaciones.dto.ubicaciones.request.UbicacionDeleteRequest;
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
@RequestMapping("/api/ubicaciones/ubicaciones")
@RequiredArgsConstructor
@Validated
public class UbicacionController {

    private final UbicacionService ubicacionService;

    @GetMapping("/getById/{id}")
    public ResponseEntity<UbicacionGetByIdResponse> getById(
        @NotNull(message = "{error.idUbicacion.notNull}")
        @Positive(message = "{error.idUbicacion.positive}")
        @PathVariable Long id
    ) {

        UbicacionGetByIdRequest request = new UbicacionGetByIdRequest(id);

        return ResponseEntity.ok(this.ubicacionService.getById(request));
    }

    @GetMapping("/getAll")
    public ResponseEntity<UbicacionGetAllResponse> getAll() {

        return ResponseEntity.ok(this.ubicacionService.getAll());
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(
        @RequestBody @Valid UbicacionRegisterRequest request
    ) {

        this.ubicacionService.register(request);

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/edit")
    public ResponseEntity<UbicacionEditResponse> edit(
        @RequestBody @Valid UbicacionEditRequest request
    ) {

        return ResponseEntity.ok(this.ubicacionService.edit(request));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(
        @NotNull(message = "{error.idUbicacion.notNull}")
        @Positive(message = "{error.idUbicacion.positive}")
        @PathVariable Long id
    ) {

        UbicacionDeleteRequest request = new UbicacionDeleteRequest(id);

        this.ubicacionService.delete(request);

        return ResponseEntity.ok().build();
    }

}

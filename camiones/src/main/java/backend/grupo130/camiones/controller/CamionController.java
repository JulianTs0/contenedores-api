package backend.grupo130.camiones.controller;

import backend.grupo130.camiones.data.models.Camion;
import backend.grupo130.camiones.dto.request.*;
import backend.grupo130.camiones.dto.response.EditResponse;
import backend.grupo130.camiones.dto.response.GetAllResponse;
import backend.grupo130.camiones.dto.response.GetByIdResponse;
import backend.grupo130.camiones.service.CamionService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/camiones")
@RequiredArgsConstructor
@Validated
@Data
public class CamionController {

    private final CamionService camionService;

    @GetMapping("/getById/{dominio}")
    public ResponseEntity<GetByIdResponse> getById(
        @NotBlank(message = "{error.dominio.notBlank}")
        @Size(max = 80, message = "{error.dominio.max}")
        @PathVariable String dominio
    ) {
        GetByIdRequest request = new GetByIdRequest(dominio);

        return ResponseEntity.ok(this.camionService.getById(request));
    }

    @GetMapping("/getAll")
    public ResponseEntity<GetAllResponse> getAll() {

        return ResponseEntity.ok(this.camionService.getAll());
    }

    @GetMapping("/getDisponibles")
    public ResponseEntity<GetAllResponse> getDisponibles() {

        return ResponseEntity.ok(this.camionService.getDisponibles());
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestBody @Valid RegisterRequest request
    ) {

        this.camionService.register(request);

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/edit")
    public ResponseEntity<EditResponse> edit(
            @RequestBody @Valid EditRequest request
    ) {

        return ResponseEntity.ok(this.camionService.edit(request));
    }

    @PatchMapping("/cambiarDisponibilidad")
    public ResponseEntity<EditResponse> cambiarDisponibilidad(
        @RequestBody @Valid CambiarDisponibilidadRequest request
    ) {

        return ResponseEntity.ok(this.camionService.cambiarDisponibilidad(request));
    }

    @PatchMapping("/asignarTransportista")
    public ResponseEntity<?> cambioDeEstado(
        @RequestBody @Valid AsignarTransportistaRequest request
    ) {
        this.camionService.asignarTransportista(request);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/{dominio}")
    public ResponseEntity<?> delete(
        @NotBlank(message = "{error.dominio.notBlank}")
        @Size(max = 80, message = "{error.dominio.max}")
        @PathVariable String dominio
    ) {

        DeleteRequest request = new DeleteRequest(dominio);

        this.camionService.delete(request);

        return ResponseEntity.ok().build();
    }

}

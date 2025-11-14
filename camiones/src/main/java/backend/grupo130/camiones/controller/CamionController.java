package backend.grupo130.camiones.controller;

import backend.grupo130.camiones.data.models.Camion;
import backend.grupo130.camiones.dto.request.EditRequest;
import backend.grupo130.camiones.dto.request.GetByIdRequest;
import backend.grupo130.camiones.dto.request.RegisterRequest;
import backend.grupo130.camiones.dto.response.EditResponse;
import backend.grupo130.camiones.dto.response.GetAllResponse;
import backend.grupo130.camiones.dto.response.GetByIdResponse;
import backend.grupo130.camiones.service.CamionService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
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
            @NotBlank(message = "El dominio no puede ser nulo o vacío")
            @PathVariable String dominio
    ) {
        GetByIdRequest request = new GetByIdRequest(dominio);

        Camion camion = this.camionService.getById(request);

        return ResponseEntity.ok(this.toResponseGet(camion));
    }

    @GetMapping("/getAll")
    public ResponseEntity<GetAllResponse> getAll() {

        List<Camion> camiones = this.camionService.getAll();

        return ResponseEntity.ok(this.toResponseGet(camiones));
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(
            @RequestBody @Valid RegisterRequest request
    ) {

        this.camionService.register(request);

        return ResponseEntity.ok().build();

    }

    @PatchMapping("/edit")
    public ResponseEntity<EditResponse> edit(
            @RequestBody @Valid EditRequest request
    ) {

        Camion camion = this.camionService.edit(request);

        return ResponseEntity.ok(this.toResponsePatch(camion));
    }

    private GetByIdResponse toResponseGet(Camion camion) {
        return new GetByIdResponse(
                camion.getDominio(),
                camion.getCapacidadPeso(),
                camion.getCapacidadVolumen(),
                camion.getConsumoCombustible(),
                camion.getCostoTrasladoBase(),
                camion.getEstado(),
                camion.getTransportista()
        );
    }

    private GetAllResponse toResponseGet(List<Camion> camiones) {
        return new GetAllResponse(camiones);
    }

    private EditResponse toResponsePatch(Camion camion) {
        return new EditResponse(
            camion.getDominio(),
            camion.getCapacidadPeso(),
            camion.getCapacidadVolumen(),
            camion.getConsumoCombustible(),
            camion.getCostoTrasladoBase(),
            camion.getEstado()
        );
    }
}

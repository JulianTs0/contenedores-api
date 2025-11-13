package backend.grupo130.envios.controller;

import backend.grupo130.envios.config.exceptions.ServiceError;
import backend.grupo130.envios.data.models.Solicitud;
import backend.grupo130.envios.dto.request.EditRequest;
import backend.grupo130.envios.dto.request.GetByIdRequest;
import backend.grupo130.envios.dto.request.RegisterRequest;
import backend.grupo130.envios.dto.response.EditResponse;
import backend.grupo130.envios.dto.response.GetAllResponse;
import backend.grupo130.envios.dto.response.GetByIdResponse;
import backend.grupo130.envios.service.SolicitudService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/envios")
@RequiredArgsConstructor
@Validated
public class SolicitudController {

    private final SolicitudService solicitudService;

    @GetMapping("/getById/{id}")
    public ResponseEntity<GetByIdResponse> getById(
            @NotNull(message = "El ID de usuario no puede ser nulo")
            @Min(value = 1, message = "El ID de usuario debe ser un número positivo")
            @PathVariable Integer id
    ) {

        GetByIdRequest request = new GetByIdRequest(id);

        Solicitud solicitud = this.solicitudService.getById(request);

        return ResponseEntity.ok(this.toResponseGet(solicitud));
    }

    @GetMapping("/getAll")
    public ResponseEntity<?> getAll() {

        List<Solicitud> solicitudes = this.solicitudService.getAll();

        return ResponseEntity.ok(this.toResponseGet(solicitudes));
    }

    @PostMapping("/register")
    public ResponseEntity<GetByIdResponse> register(
            @RequestBody @Valid RegisterRequest request
    ) {

        this.solicitudService.register(request);

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/edit")
    public ResponseEntity<EditResponse> edit(
            @RequestBody @Valid EditRequest request
    ) {

        Solicitud solicitud = this.solicitudService.edit(request);

        return ResponseEntity.ok(this.toResponsePatch(solicitud));
    }

    // Respuestas

    private GetByIdResponse toResponseGet(Solicitud solicitud) {
        return new GetByIdResponse(
                solicitud.getFechaInicio(),
                solicitud.getCostoEstimado(),
                solicitud.getTiempoEstimadoEnHoras(),
                solicitud.getCostoFinal(),
                usuario.getRol().name()
        );
    }

    private GetAllResponse toResponseGet(List<Solicitud> solicitud) {
        return new GetAllResponse(
                usuarios
        );
    }

    private EditResponse toResponsePatch(Solicitud solicitud) {
        return new EditResponse(
                usuario.getNombre(),
                usuario.getApellido(),
                usuario.getTelefono(),
                usuario.getEmail()
        );
    }

}

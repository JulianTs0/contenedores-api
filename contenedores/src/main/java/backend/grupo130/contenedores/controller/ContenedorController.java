package backend.grupo130.contenedores.controller;

import backend.grupo130.contenedores.data.models.Contenedor;
import backend.grupo130.contenedores.dto.request.CambioDeEstadoRequest;
import backend.grupo130.contenedores.dto.request.EditRequest;
import backend.grupo130.contenedores.dto.request.GetByIdRequest;
import backend.grupo130.contenedores.dto.request.RegisterRequest;
import backend.grupo130.contenedores.dto.response.EditResponse;
import backend.grupo130.contenedores.dto.response.GetAllResponse;
import backend.grupo130.contenedores.dto.response.GetByIdResponse;
import backend.grupo130.contenedores.service.ContenedorService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contenedores")
@RequiredArgsConstructor
@Validated
public class ContenedorController {

    private final ContenedorService contenedorService;

    @GetMapping("/getById/{id}")
    public ResponseEntity<GetByIdResponse> getById(
        @NotNull(message = "El ID de contenedor no puede ser nulo")
        @Positive(message = "El ID del contenedor debe ser un número positivo")
        @PathVariable Integer id
    ) {

        GetByIdRequest request = new GetByIdRequest(id);

        Contenedor contenedor = this.contenedorService.getById(request);

        return ResponseEntity.ok(this.toResponseGet(contenedor));
    }

    @GetMapping("/getAll")
    public ResponseEntity<?> getAll() {

        List<Contenedor> contenedores = this.contenedorService.getAll();

        return ResponseEntity.ok(this.toResponseGet(contenedores));
    }

    @PostMapping("/register")
    public ResponseEntity<GetByIdResponse> register(
        @RequestBody @Valid RegisterRequest request
    ) {

        this.contenedorService.register(request);

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/edit")
    public ResponseEntity<EditResponse> edit(
        @RequestBody @Valid EditRequest request
    ) {

        Contenedor contenedor = this.contenedorService.edit(request);

        return ResponseEntity.ok(this.toResponsePatch(contenedor));
    }

    @PatchMapping("/cambioDeEstado")
    public ResponseEntity<EditResponse> cambioDeEstado(
        @RequestBody @Valid CambioDeEstadoRequest request
    ) {

        this.contenedorService.cambioDeEstado(request);

        return ResponseEntity.ok().build();
    }

    // Respuestas

    private GetByIdResponse toResponseGet(Contenedor contenedor) {
        return new GetByIdResponse(
            contenedor.getIdContenedor(),
            contenedor.getPeso(),
            contenedor.getVolumen(),
            contenedor.getIdCliente(),
            contenedor.getEstado().name()
        );
    }

    private GetAllResponse toResponseGet(List<Contenedor> contenedores) {
        return new GetAllResponse(
            contenedores
        );
    }

    private EditResponse toResponsePatch(Contenedor contenedor) {
        return new EditResponse(
            contenedor.getPeso(),
            contenedor.getPeso(),
            contenedor.getIdCliente()
        );
    }

}

package backend.grupo130.contenedores.controller;

import backend.grupo130.contenedores.dto.request.*;
import backend.grupo130.contenedores.dto.response.*;
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
        @NotNull(message = "{error.idContenedor.notNull}")
        @Positive(message = "{error.idContenedor.positive}")
        @PathVariable Long id
    ) {

        GetByIdRequest request = new GetByIdRequest(id);

        return ResponseEntity.ok(this.contenedorService.getById(request));
    }

    @PostMapping("/getByPesoVolumen")
    public ResponseEntity<GetByPesoVolumenResponse> getByPesoVolumen(
        @RequestBody @Valid GetByPesoVolumenRequest request
    ) {

        return ResponseEntity.ok(this.contenedorService.getByPesoVolumen(request));
    }

    @GetMapping("/getByEstado/{estado}")
    public ResponseEntity<GetAllResponse> getByEstado(
        @NotNull(message = "{error.estado.notNull}")
        @PathVariable String estado
    ) {

        GetByEstado request = new GetByEstado(estado);

        return ResponseEntity.ok(this.contenedorService.getByEstado(request));
    }

    @GetMapping("/getAll")
    public ResponseEntity<?> getAll() {


        return ResponseEntity.ok(this.contenedorService.getAll());
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(
        @RequestBody @Valid RegisterRequest request
    ) {

        this.contenedorService.register(request);

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/edit")
    public ResponseEntity<EditResponse> edit(
        @RequestBody @Valid EditRequest request
    ) {

        return ResponseEntity.ok(this.contenedorService.edit(request));
    }

    @PatchMapping("/cambioDeEstado")
    public ResponseEntity<CambioDeEstadoResponse> cambioDeEstado(
        @RequestBody @Valid CambioDeEstadoRequest request
    ) {

        return ResponseEntity.ok(this.contenedorService.cambioDeEstado(request));
    }

    @PatchMapping("/asignarCliente")
    public ResponseEntity<?> cambioDeEstado(
        @RequestBody @Valid AsignarClienteRequest request
    ) {

        this.contenedorService.asignarCliente(request);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(
        @NotNull(message = "{error.idContenedor.notNull}")
        @Positive(message = "{error.idContenedor.positive}")
        @PathVariable Long id
    ) {

        DeleteRequest request = new DeleteRequest(id);

        this.contenedorService.delete(request);

        return ResponseEntity.ok().build();
    }

}

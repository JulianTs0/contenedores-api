package backend.grupo130.tramos.controller;

import backend.grupo130.tramos.data.models.Tramo;
import backend.grupo130.tramos.dto.ruta.request.RutaRegisterRequest;
import backend.grupo130.tramos.dto.tramo.request.*;
import backend.grupo130.tramos.dto.tramo.response.TramoGetAllResponse;
import backend.grupo130.tramos.dto.tramo.response.TramoGetByIdResponse;
import backend.grupo130.tramos.service.TramoService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tramos")
@RequiredArgsConstructor
@Validated
public class TramoController {

    private final TramoService tramoService;

    @GetMapping("/getById/{id}")
    public ResponseEntity<TramoGetByIdResponse> getById(
        @NotNull(message = "El ID del tramo no puede ser nulo")
        @Positive(message = "El ID del tramo debe ser un número positivo")
        @PathVariable Integer id
    ) {

        TramoGetByIdRequest request = new TramoGetByIdRequest(id);

        return ResponseEntity.ok(this.tramoService.getById(request));
    }

    @GetMapping("/getAll")
    public ResponseEntity<TramoGetAllResponse> getAll() {


        return ResponseEntity.ok(this.tramoService.getAll());
    }

    @PatchMapping("/asignarCamion")
    public ResponseEntity<?> asignarCamion(
        @RequestBody @Valid TramoAsignacionCamionRequest request
    ) {

        this.tramoService.asignarCamion(request);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/getByTransportista/{dominio}")
    public ResponseEntity<TramoGetAllResponse> getByTransportista(
        @NotNull(message = "El dominio no puede ser nulo")
        @NotEmpty(message = "El dominio del tramo no puede estar vacio")
        @PathVariable String dominio
    ) {

        TramoGetByTransportistaRequest request = new TramoGetByTransportistaRequest(dominio);

        return ResponseEntity.ok(this.tramoService.getByTransportista(request));
    }

    @GetMapping("/getByRuta/{id}")
    public ResponseEntity<TramoGetAllResponse> getByRuta(
        @NotNull(message = "La id de la ruta no puede ser nulo")
        @Positive(message = "La id de la ruta debe ser un número positivo")
        @PathVariable Integer id
    ) {

        TramoGetByRutaIdRequest request = new TramoGetByRutaIdRequest(id);

        return ResponseEntity.ok(this.tramoService.getTramosDeRuta(request));
    }

    @PatchMapping("/registrarInicio")
    public ResponseEntity<?> registrarInicio(
        @RequestBody @Valid TramoInicioTramoRequest request
    ) {

        this.tramoService.registrarInicioTramo(request);

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/registrarFin")
    public ResponseEntity<?> registrarFin(
        @RequestBody @Valid TramoFinTramoRequest request
    ) {

        this.tramoService.registrarFinTramo(request);

        return ResponseEntity.ok().build();
    }

    /* @PostMapping("/register")
    public ResponseEntity<?> register(
        @RequestBody @Valid TramoRegisterRequest request
    ) {

        this.tramoService.register(request);

        return ResponseEntity.ok().build();
    } */

   /* @PatchMapping("/edit")
    public ResponseEntity<EditResponse> edit(
        @RequestBody @Valid EditRequest request
    ) {

        Contenedor contenedor = this.contenedorService.edit(request);

        return ResponseEntity.ok(this.toResponsePatch(contenedor));
    }*/

}

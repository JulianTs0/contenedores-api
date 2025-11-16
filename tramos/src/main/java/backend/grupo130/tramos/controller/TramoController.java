package backend.grupo130.tramos.controller;

import backend.grupo130.tramos.data.models.Tramo;
import backend.grupo130.tramos.dto.ruta.request.RutaRegisterRequest;
import backend.grupo130.tramos.dto.tramo.request.*;
import backend.grupo130.tramos.dto.tramo.response.TramoGetAllResponse;
import backend.grupo130.tramos.dto.tramo.response.TramoGetByIdResponse;
import backend.grupo130.tramos.service.TramoService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
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

    @GetMapping("/getById/{idTramo}")
    public ResponseEntity<TramoGetByIdResponse> getById(
        @NotNull(message = "{error.idTramo.notNull}")
        @Positive(message = "{error.idTramo.positive}")
        @PathVariable Long idTramo
    ) {

        TramoGetByIdRequest request = new TramoGetByIdRequest(idTramo);

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
        @NotBlank(message = "{error.dominioCamion.notBlank}")
        @PathVariable String dominio
    ) {

        TramoGetByTransportistaRequest request = new TramoGetByTransportistaRequest(dominio);

        return ResponseEntity.ok(this.tramoService.getByTransportista(request));
    }

    @GetMapping("/getByRuta/{idRuta}")
    public ResponseEntity<TramoGetAllResponse> getByRuta(
        @NotNull(message = "{error.idRuta.notNull}")
        @Positive(message = "{error.idRuta.positive}")
        @PathVariable Long idRuta
    ) {

        TramoGetByRutaIdRequest request = new TramoGetByRutaIdRequest(idRuta);

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

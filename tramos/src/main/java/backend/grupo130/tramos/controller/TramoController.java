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

        Tramo tramo = this.tramoService.getById(request);

        return ResponseEntity.ok(this.toResponseGet(tramo));
    }

    @GetMapping("/getAll")
    public ResponseEntity<TramoGetAllResponse> getAll() {

        List<Tramo> tramos = this.tramoService.getAll();

        return ResponseEntity.ok(this.toResponseGet(tramos));
    }

    @PatchMapping("/asignarCamion")
    public ResponseEntity<?> asignarCamion(
        @RequestBody @Valid TramoAsignacionCamionRequest request
    ) {

        this.tramoService.asignarCamion(request);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/getByTransportista/{dominio}")
    public ResponseEntity<TramoGetAllResponse> getByTransportista(
        @NotNull(message = "El dominio no puede ser nulo")
        @NotEmpty(message = "El dominio del tramo no puede estar vacio")
        @PathVariable String dominio
    ) {

        TramoGetByTransportistaRequest request = new TramoGetByTransportistaRequest(dominio);

        List<Tramo> tramos = this.tramoService.getByTransportista(request);

        return ResponseEntity.ok(this.toResponseGet(tramos));
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

    // Respuestas

    private TramoGetByIdResponse toResponseGet(Tramo tramo) {
        return new TramoGetByIdResponse(
            tramo.getIdTramo(),
            tramo.getTipoTramo().name(),
            tramo.getEstado().name(),
            tramo.getCostoAproximado(),
            tramo.getCostoReal(),
            tramo.getFechaHoraInicioEstimado(),
            tramo.getFechaHoraFinEstimado(),
            tramo.getFechaHoraInicioReal(),
            tramo.getFechaHoraFinReal(),
            tramo.getOrden(),
            tramo.getCamion(),
            tramo.getRutaTraslado(),
            tramo.getOrigen(),
            tramo.getDestino()
        );
    }

    private TramoGetAllResponse toResponseGet(List<Tramo> tramos) {
        return new TramoGetAllResponse(
            tramos
        );
    }


}

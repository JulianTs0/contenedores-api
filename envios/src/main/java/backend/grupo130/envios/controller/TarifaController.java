package backend.grupo130.envios.controller;

import backend.grupo130.envios.dto.tarifa.request.TarifaRegisterRequest;
import backend.grupo130.envios.dto.tarifa.response.TarifaRegisterResponse;
import backend.grupo130.envios.service.TarifaService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/envios/tarifa")
@RequiredArgsConstructor
@Validated
public class TarifaController {

    private final TarifaService tarifaService;

    @PostMapping("/register")
    public ResponseEntity<TarifaRegisterResponse> register(
            @RequestBody @Valid TarifaRegisterRequest request
    ) {


        return ResponseEntity.ok(this.tarifaService.register(request));
    }

}

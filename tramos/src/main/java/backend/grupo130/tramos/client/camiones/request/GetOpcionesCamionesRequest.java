package backend.grupo130.tramos.client.camiones.request;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class GetOpcionesCamionesRequest {

    private BigDecimal capacidadPeso;

    private BigDecimal capacidadVolumen;

}

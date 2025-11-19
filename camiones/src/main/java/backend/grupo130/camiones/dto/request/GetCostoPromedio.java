package backend.grupo130.camiones.dto.request;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class GetCostoPromedio {

    @NotNull(message = "{error.capacidadPeso.notNull}")
    @Positive(message = "{error.capacidadPeso.positve}")
    @Digits(integer = 10, fraction = 2, message = "{error.capacidadPeso.digits}")
    private BigDecimal capacidadPeso;

    @NotNull(message = "{error.capacidadVolumen.notNull}")
    @Positive(message = "{error.capacidadVolumen.positve}")
    @Digits(integer = 10, fraction = 2, message = "{error.capacidadVolumen.digits}")
    private BigDecimal capacidadVolumen;

}


package backend.grupo130.camiones.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
@AllArgsConstructor
public class EditRequest {

    private final String dominio;

    @Positive(message = "{error.capacidadPeso.positive}")
    @Digits(integer = 10, fraction = 2, message = "{error.capacidadPeso.digits}")
    private final BigDecimal capacidadPeso;

    @Positive(message = "{error.capacidadVolumen.positive}")
    @Digits(integer = 10, fraction = 2, message = "{error.capacidadVolumen.digits}")
    private final BigDecimal capacidadVolumen;

    @Positive(message = "{error.consumoCombustible.positive}")
    @Digits(integer = 10, fraction = 2, message = "{error.consumoCombustible.digits}")
    private final BigDecimal consumoCombustible;

    @PositiveOrZero(message = "{error.costoTrasladoBase.positiveOrZero}")
    @Digits(integer = 10, fraction = 2, message = "{error.costoTrasladoBase.digits}")
    private final BigDecimal costoTrasladoBase;

}

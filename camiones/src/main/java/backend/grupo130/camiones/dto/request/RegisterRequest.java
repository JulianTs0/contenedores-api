
package backend.grupo130.camiones.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "{error.dominio.notBlank}")
    @Size(max = 80, message = "{error.dominio.max}")
    private final String dominio;

    @NotNull(message = "{error.capacidadPeso.notNull}")
    @Positive(message = "{error.capacidadPeso.positive}")
    @Digits(integer = 10, fraction = 2, message = "{error.capacidadPeso.digits}")
    private final BigDecimal capacidadPeso;

    @NotNull(message = "{error.capacidadVolumen.notNull}")
    @Positive(message = "{error.capacidadVolumen.positive}")
    @Digits(integer = 10, fraction = 2, message = "{error.capacidadVolumen.digits}")
    private final BigDecimal capacidadVolumen;

    @NotNull(message = "{error.consumoCombustible.notNull}")
    @Positive(message = "{error.consumoCombustible.positive}")
    @Digits(integer = 10, fraction = 2, message = "{error.consumoCombustible.digits}")
    private final BigDecimal consumoCombustible;

    @NotNull(message = "{error.costoTrasladoBase.notNull}")
    @PositiveOrZero(message = "{error.costoTrasladoBase.positiveOrZero}")
    @Digits(integer = 10, fraction = 2, message = "{error.costoTrasladoBase.digits}")
    private final BigDecimal costoTrasladoBase;

}

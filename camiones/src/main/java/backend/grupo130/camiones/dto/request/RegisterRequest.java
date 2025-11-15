
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
    private String dominio;

    @NotNull(message = "{error.capacidadPeso.notNull}")
    @Positive(message = "{error.capacidadPeso.positve}")
    @Digits(integer = 10, fraction = 2, message = "{error.capacidadPeso.digits}")
    private BigDecimal capacidadPeso;

    @NotNull(message = "{error.capacidadVolumen.notNull}")
    @Positive(message = "{error.capacidadVolumen.positve}")
    @Digits(integer = 10, fraction = 2, message = "{error.capacidadVolumen.digits}")
    private BigDecimal capacidadVolumen;

    @NotNull(message = "{error.consumoCombustible.notNull}")
    @Positive(message = "{error.consumoCombustible.positve}")
    @Digits(integer = 10, fraction = 2, message = "{error.consumoCombustible.digits}")
    private BigDecimal consumoCombustible;

    @NotNull(message = "{error.costoTrasladoBase.notNull}")
    @PositiveOrZero(message = "{error.costoTrasladoBase.positiveOrZero}")
    @Digits(integer = 10, fraction = 2, message = "{error.costoTrasladoBase.digits}")
    private BigDecimal costoTrasladoBase;

}

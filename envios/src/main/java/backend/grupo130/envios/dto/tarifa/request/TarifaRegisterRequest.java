package backend.grupo130.envios.dto.tarifa.request;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class TarifaRegisterRequest {

    @NotNull(message = "{error.volumenMin.notNull}")
    @PositiveOrZero(message = "{error.volumenMin.positiveOrZero}")
    @Digits(integer = 8, fraction = 2, message = "{error.volumenMin.digits}")
    private BigDecimal volumenMin;

    @NotNull(message = "{error.volumenMax.notNull}")
    @Positive(message = "{error.volumenMax.positive}")
    @Digits(integer = 8, fraction = 2, message = "{error.volumenMax.digits}")
    private BigDecimal volumenMax;

    @NotNull(message = "{error.costoBase.notNull}")
    @Positive(message = "{error.costoBase.positive}")
    @Digits(integer = 8, fraction = 2, message = "{error.costoBase.digits}")
    private BigDecimal costoBase;

    @NotNull(message = "{error.valorLitro.notNull}")
    @Positive(message = "{error.valorLitro.positive}")
    @Digits(integer = 8, fraction = 2, message = "{error.valorLitro.digits}")
    private BigDecimal valorLitro;

    @NotNull(message = "{error.consumoAprox.notNull}")
    @PositiveOrZero(message = "{error.consumoAprox.positiveOrZero}")
    @Digits(integer = 8, fraction = 2, message = "{error.consumoAprox.digits}")
    private BigDecimal consumoAprox;

    @NotNull(message = "{error.costoEstadia.notNull}")
    @Positive(message = "{error.costoEstadia.positive}")
    @Digits(integer = 8, fraction = 2, message = "{error.costoEstadia.digits}")
    private BigDecimal costoEstadia;

}

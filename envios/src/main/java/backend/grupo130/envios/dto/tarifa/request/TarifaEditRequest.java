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
public class TarifaEditRequest {

    @NotNull(message = "{error.idTarifa.notNull}")
    @Positive(message = "{error.idTarifa.positive}")
    private final Long idTarifa;

    @PositiveOrZero(message = "{error.volumenMin.positiveOrZero}")
    @Digits(integer = 8, fraction = 2, message = "{error.volumenMin.digits}")
    private final BigDecimal volumenMin;

    @Positive(message = "{error.volumenMax.positive}")
    @Digits(integer = 8, fraction = 2, message = "{error.volumenMax.digits}")
    private final BigDecimal volumenMax;

    @Positive(message = "{error.costoBase.positive}")
    @Digits(integer = 8, fraction = 2, message = "{error.costoBase.digits}")
    private final BigDecimal costoBase;

    @Positive(message = "{error.valorLitro.positive}")
    @Digits(integer = 8, fraction = 2, message = "{error.valorLitro.digits}")
    private final BigDecimal valorLitro;

    @PositiveOrZero(message = "{error.consumoAprox.positiveOrZero}")
    @Digits(integer = 8, fraction = 2, message = "{error.consumoAprox.digits}")
    private final BigDecimal consumoAprox;

    @Positive(message = "{error.costoEstadia.positive}")
    @Digits(integer = 8, fraction = 2, message = "{error.costoEstadia.digits}")
    private final BigDecimal costoEstadia;
}
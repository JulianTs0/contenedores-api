package backend.grupo130.envios.data.entity;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Tarifa {

    private Long idTarifa;

    @NotNull(message = "{error.pesoMax.notNull}")
    @Positive(message = "{error.pesoMax.positive}")
    @Digits(integer = 8, fraction = 2, message = "{error.pesoMax.digits}")
    private BigDecimal pesoMax;

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

    @PositiveOrZero(message = "{error.costoEstadia.positive}")
    @Digits(integer = 8, fraction = 2, message = "{error.costoEstadia.digits}")
    private BigDecimal costoEstadia;

    @Positive
    @Digits(integer = 8, fraction = 2)
    private BigDecimal costoEstimado;

    @Positive
    @Digits(integer = 8, fraction = 2)
    private BigDecimal costoFinal;

}

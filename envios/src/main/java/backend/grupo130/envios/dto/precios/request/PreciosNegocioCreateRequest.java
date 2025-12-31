package backend.grupo130.envios.dto.precios.request;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class PreciosNegocioCreateRequest {

    @NotNull(message = "{error.rangoPesoBajo.notNull}")
    @Positive(message = "{error.rangoPesoBajo.positive}")
    @Digits(integer = 8, fraction = 2, message = "{error.rangoPesoBajo.digits}")
    private final BigDecimal rangoPesoBajo;

    @NotNull(message = "{error.rangoPesoMedio.notNull}")
    @Positive(message = "{error.rangoPesoMedio.positive}")
    @Digits(integer = 8, fraction = 2, message = "{error.rangoPesoMedio.digits}")
    private final BigDecimal rangoPesoMedio;

    @NotNull(message = "{error.multiplicadorBajo.notNull}")
    @Positive(message = "{error.multiplicadorBajo.positive}")
    @Digits(integer = 8, fraction = 2, message = "{error.multiplicadorBajo.digits}")
    private final BigDecimal multiplicadorBajo;

    @NotNull(message = "{error.multiplicadorMedio.notNull}")
    @Positive(message = "{error.multiplicadorMedio.positive}")
    @Digits(integer = 8, fraction = 2, message = "{error.multiplicadorMedio.digits}")
    private final BigDecimal multiplicadorMedio;

    @NotNull(message = "{error.multiplicadorAlto.notNull}")
    @Positive(message = "{error.multiplicadorAlto.positive}")
    @Digits(integer = 8, fraction = 2, message = "{error.multiplicadorAlto.digits}")
    private final BigDecimal multiplicadorAlto;

    @NotNull(message = "{error.valorLitro.notNull}")
    @Positive(message = "{error.valorLitro.positive}")
    @Digits(integer = 8, fraction = 2, message = "{error.valorLitro.digits}")
    private final BigDecimal valorLitro;

    @NotNull(message = "{error.cargoGestion.notNull}")
    @Positive(message = "{error.cargoGestion.positive}")
    @Digits(integer = 8, fraction = 2, message = "{error.cargoGestion.digits}")
    private final BigDecimal cargoGestion;
}

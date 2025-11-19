package backend.grupo130.contenedores.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class EditRequest {

    @NotNull(message = "{error.idContenedor.notNull}")
    @Positive(message = "{error.idContenedor.positive}")
    private final Long id;

    @Positive(message = "{error.peso.positive}")
    @Digits(integer = 8, fraction = 2, message = "{error.peso.digits}")
    private final BigDecimal peso;

    @Positive(message = "{error.volumen.positive}")
    @Digits(integer = 8, fraction = 2, message = "{error.volumen.digits}")
    private final BigDecimal volumen;

}

package backend.grupo130.contenedores.dto.request;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
@AllArgsConstructor
public class EditRequest {

    private final Long id;

    @Positive(message = "{error.peso.positive}")
    @Digits(integer = 8, fraction = 2, message = "{error.peso.digits}")
    private final BigDecimal peso;

    @Positive(message = "{error.volumen.positive}")
    @Digits(integer = 8, fraction = 2, message = "{error.volumen.digits}")
    private final BigDecimal volumen;

}

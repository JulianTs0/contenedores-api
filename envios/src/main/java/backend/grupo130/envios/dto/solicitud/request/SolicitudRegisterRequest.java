package backend.grupo130.envios.dto.solicitud.request;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class SolicitudRegisterRequest {

    @NotNull(message = "{error.peso.notNull}")
    @Positive(message = "{error.peso.positive}")
    @Digits(integer = 8, fraction = 2, message = "{error.peso.digits}")
    private final BigDecimal peso;

    @NotNull(message = "{error.volumen.notNull}")
    @Positive(message = "{error.volumen.positive}")
    @Digits(integer = 8, fraction = 2, message = "{error.volumen.digits}")
    private final BigDecimal volumen;

    @NotNull(message = "{error.idCliente.notNull}")
    @Positive(message = "{error.idCliente.positive}")
    private final Long idCliente;

}

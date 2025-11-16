
package backend.grupo130.ubicaciones.dto.deposito.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class DepositoEditRequest {

    @NotNull(message = "{error.idDeposito.notNull}")
    @Positive(message = "{error.idDeposito.positive}")
    private final Long idDeposito;

    @Size(min = 1, message = "{error.nombreDeposito.size}")
    private final String nombre;

    @Positive(message = "{error.costoEstadiaDiario.positive}")
    @Digits(integer = 8, fraction = 2, message = "{error.costoEstadiaDiario.digits}")
    private final BigDecimal costoEstadiaDiario;

}

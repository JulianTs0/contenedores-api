
package backend.grupo130.ubicaciones.dto.deposito.request;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class DepositoRegisterRequest {

    @NotBlank(message = "{error.nombreDeposito.notBlank}")
    private final String nombre;

    @NotNull(message = "{error.costoEstadiaDiario.notNull}")
    @Positive(message = "{error.costoEstadiaDiario.positive}")
    @Digits(integer = 8, fraction = 2, message = "{error.costoEstadiaDiario.digits}")
    private final BigDecimal costoEstadiaDiario;

    @NotNull(message = "{error.idUbicacion.notNull}")
    @Positive(message = "{error.idUbicacion.positive}")
    private final Long idUbicacion;

}

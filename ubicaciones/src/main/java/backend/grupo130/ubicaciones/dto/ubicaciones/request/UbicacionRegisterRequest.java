
package backend.grupo130.ubicaciones.dto.ubicaciones.request;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class UbicacionRegisterRequest {

    @NotBlank(message = "{error.direccion.notBlank}")
    private final String direccion;

    @NotNull(message = "{error.latitud.notNull}")
    @Digits(integer = 10, fraction = 4, message = "{error.latitud.digits}")
    private final BigDecimal latitud;

    @NotNull(message = "{error.longitud.notNull}")
    @Digits(integer = 10, fraction = 4, message = "{error.longitud.digits}")
    private final BigDecimal longitud;

}


package backend.grupo130.ubicaciones.dto.ubicaciones.request;

import jakarta.validation.constraints.*;
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
    @DecimalMin(value = "-56.0", message = "{error.latitud.range}")
    @DecimalMax(value = "-21.0", message = "{error.latitud.range}")
    private final BigDecimal latitud;

    @NotNull(message = "{error.longitud.notNull}")
    @Digits(integer = 10, fraction = 4, message = "{error.longitud.digits}")
    @DecimalMin(value = "-74.0", message = "{error.longitud.range}")
    @DecimalMax(value = "-53.0", message = "{error.longitud.range}")
    private final BigDecimal longitud;

}

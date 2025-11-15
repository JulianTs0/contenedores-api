
package backend.grupo130.ubicaciones.dto.ubicaciones.request;

import backend.grupo130.ubicaciones.data.models.Deposito;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class UbicacionEditRequest {

    @NotNull(message = "{error.idUbicacion.notNull}")
    @Positive(message = "{error.idUbicacion.positive}")
    private final Long idUbicacion;

    @Size(min = 1, message = "{error.direccion.notBlank}")
    private final String direccion;

    @Digits(integer = 10, fraction = 4, message = "{error.latitud.digits}")
    private final BigDecimal latitud;

    @Digits(integer = 10, fraction = 4, message = "{error.longitud.digits}")
    private final BigDecimal longitud;

}

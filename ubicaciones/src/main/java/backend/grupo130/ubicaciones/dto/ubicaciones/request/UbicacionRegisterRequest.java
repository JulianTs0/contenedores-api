
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

    @NotNull(message = "La direccion es obligatoria")
    @NotBlank(message = "La direccion no puede estar vacia")
    private final String direccion;

    @NotNull(message = "La latitud es obligatoria")
    @Digits(integer = 8, fraction = 2, message = "El formato la latitud no es válido")
    private final BigDecimal latitud;

    @NotNull(message = "La longitudes obligatoria")
    @Digits(integer = 8, fraction = 2, message = "El formato la latitud no es válido")
    private final BigDecimal longitud;

}

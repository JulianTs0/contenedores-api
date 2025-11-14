
package backend.grupo130.ubicaciones.dto.ubicaciones.request;

import backend.grupo130.ubicaciones.data.models.Deposito;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class UbicacionEditRequest {

    @NotNull(message = "La id es obligatoria")
    @Positive(message = "El ID del contenedor debe ser un número positivo")
    private final Integer ubicacionId;

    @NotBlank(message = "La direccion no puede estar vacia")
    private final String direccion;

    @Positive(message = "La latitud debe ser un número positivo")
    @Digits(integer = 8, fraction = 2, message = "El formato del peso no es válido")
    private final BigDecimal latitud;

    @Positive(message = "La longitud debe ser un número positivo")
    @Digits(integer = 8, fraction = 2, message = "El formato del peso no es válido")
    private final BigDecimal longitud;

}


package backend.grupo130.camiones.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "El dominio es obligatorio")
    @Size(max = 80, message = "El dominio no puede exceder los 80 caracteres")
    private String dominio;

    @NotNull(message = "La capacidad de peso es obligatoria")
    @Positive(message = "La capacidad de peso debe ser un número positivo")
    @Digits(integer = 10, fraction = 2, message = "El formato de la capacidad de peso no es válido (max 10 enteros, 2 decimales)")
    private BigDecimal capacidadPeso;

    @NotNull(message = "La capacidad de volumen es obligatoria")
    @Positive(message = "La capacidad de volumen debe ser un número positivo")
    @Digits(integer = 10, fraction = 2, message = "El formato de la capacidad de volumen no es válido (max 10 enteros, 2 decimales)")
    private BigDecimal capacidadVolumen;

    @NotNull(message = "El consumo de combustible es obligatorio")
    @Positive(message = "El consumo de combustible debe ser un número positivo")
    @Digits(integer = 10, fraction = 2, message = "El formato del consumo de combustible no es válido (max 10 enteros, 2 decimales)")
    private BigDecimal consumoCombustible;

    @NotNull(message = "El costo de traslado base es obligatorio")
    @PositiveOrZero(message = "El costo de traslado base debe ser 0 o un número positivo")
    @Digits(integer = 10, fraction = 2, message = "El formato del costo de traslado no es válido (max 10 enteros, 2 decimales)")
    private BigDecimal costoTrasladoBase;

}

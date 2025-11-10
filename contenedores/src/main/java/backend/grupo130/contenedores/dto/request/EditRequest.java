package backend.grupo130.contenedores.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class EditRequest {

    @NotNull(message = "La id es obligatoria")
    @Positive(message = "El ID del contenedor debe ser un número positivo")
    private final Integer id;

    @Positive(message = "El peso debe ser un número positivo")
    @Digits(integer = 8, fraction = 2, message = "El formato del peso no es válido")
    private final BigDecimal peso;

    @Positive(message = "El volumen debe ser un número positivo")
    @Digits(integer = 8, fraction = 2, message = "El formato del volumen no es válido")
    private final BigDecimal volumen;

    @Positive(message = "El ID del cliente debe ser un número positivo")
    private final Integer idCliente;

}

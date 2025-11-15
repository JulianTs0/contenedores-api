package backend.grupo130.contenedores.dto.request;

import backend.grupo130.contenedores.config.enums.Estado;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class RegisterRequest {

    @NotNull(message = "El peso es obligatorio")
    @Positive(message = "El peso debe ser un número positivo")
    @Digits(integer = 8, fraction = 2, message = "El formato del peso no es válido")
    private final BigDecimal peso;

    @NotNull(message = "El volumen es obligatorio")
    @Positive(message = "El volumen debe ser un número positivo")
    @Digits(integer = 8, fraction = 2, message = "El formato del volumen no es válido")
    private final BigDecimal volumen;

    @NotNull(message = "La id es obligatoria")
    @Positive(message = "El ID del cliente debe ser un número positivo")
    private final Long idCliente;

}

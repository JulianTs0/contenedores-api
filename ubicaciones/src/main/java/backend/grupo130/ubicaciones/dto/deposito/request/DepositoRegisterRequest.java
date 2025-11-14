
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

    @NotNull(message = "El nombre es obligatorio")
    @NotBlank(message = "El nombre no puede estar vacio")
    private final String nombre;

    @NotNull(message = "El costoEstadiaDiario es obligatoria")
    @Positive(message = "El costoEstadiaDiario debe ser un número positivo")
    @Digits(integer = 8, fraction = 2, message = "El formato del costoEstadiaDiario no es válido")
    private final BigDecimal costoEstadiaDiario;

    @NotNull(message = "La ubicacion es obligatoria")
    @Positive(message = "El ID del ubicacion debe ser un número positivo")
    private final Integer idUbicacion;

}

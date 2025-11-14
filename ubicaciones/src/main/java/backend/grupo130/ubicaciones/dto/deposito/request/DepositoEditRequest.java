
package backend.grupo130.ubicaciones.dto.deposito.request;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class DepositoEditRequest {

    @Positive(message = "El ID del deposito debe ser un número positivo")
    private final Integer idDeposito;

    @Size(min = 1, message = "El nombre no puede estar vacío")
    private final String nombre;

    @Positive(message = "El costo estadia diario debe ser un número positivo")
    @Digits(integer = 8, fraction = 2, message = "El formato del costo estadia diario no es válido")
    private final BigDecimal costoEstadiaDiario;

    @Positive(message = "El ID del ubicacion debe ser un número positivo")
    private final Integer idUbicacion;

}

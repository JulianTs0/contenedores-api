package backend.grupo130.tramos.dto.tramo.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class TramoFinTramoRequest {

    @NotNull(message = "La id del tramo es obligatoria")
    @Positive(message = "El ID del tramo debe ser un número positivo")
    private final Integer idTramo;

    @NotNull(message = "La dominio del camion es obligatorio")
    @NotBlank(message = "El dominio no puede estar vacio")
    private final String dominioCamion;

}

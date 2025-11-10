package backend.grupo130.tramos.dto.ruta.request;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class RutaRegisterRequest {

    @NotNull(message = "La id es obligatoria")
    @Positive(message = "El ID del solcitud debe ser un número positivo")
    private final Integer idSolicitud;

    @NotNull(message = "El cargo de gestion es obligatorio")
    @Positive(message = "El cargo de gestion debe ser un número positivo")
    @Digits(integer = 8, fraction = 2, message = "El formato del cargo de gestion no es válido")
    private final BigDecimal cargosGestionFijo;

    @NotNull(message = "Los tramos son obligatorios")
    @NotEmpty(message = "Debe haber un tramo minimo")
    private final List<Integer> ubicaciones;

    @NotNull(message = "La fecha Hora Inicio Estimada es obligatorio")
    private LocalDateTime fechaHoraInicioEstimado;

}

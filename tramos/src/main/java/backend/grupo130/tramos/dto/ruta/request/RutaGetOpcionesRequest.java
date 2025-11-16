package backend.grupo130.tramos.dto.ruta.request;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class RutaGetOpcionesRequest {

    @NotNull(message = "{error.idSolicitud.notNull}")
    @Positive(message = "{error.idSolicitud.positive}")
    private final Long idSolicitud;

    @NotNull(message = "{error.cargosGestion.notNull}")
    @Positive(message = "{error.cargosGestion.positive}")
    @Digits(integer = 8, fraction = 2, message = "{error.cargosGestion.digits}")
    private final BigDecimal cargosGestionFijo;

    @NotNull(message = "{error.ubicaciones.notNull}")
    @Size(min = 2, message = "{error.ubicaciones.sizeMin}")
    private final List<Long> ubicaciones;

    @NotNull(message = "{error.fechaHoraInicio.notNull}")
    private LocalDateTime fechaHoraInicioEstimado;

}

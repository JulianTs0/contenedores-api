package backend.grupo130.envios.dto.tarifa.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TarifaGetByIdRequest {

    @NotNull(message = "{error.idTarifa.notNull}")
    @Positive(message = "{error.idTarifa.positive}")
    private final Long idTarifa;
}
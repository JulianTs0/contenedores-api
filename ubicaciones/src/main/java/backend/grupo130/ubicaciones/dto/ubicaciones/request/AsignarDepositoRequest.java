package backend.grupo130.ubicaciones.dto.ubicaciones.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AsignarDepositoRequest {

    @NotNull(message = "{error.idUbicacion.notNull}")
    @Positive(message = "{error.idUbicacion.positive}")
    private final Long idUbicacion;

    @NotNull(message = "{error.idCliente.notNull}")
    @Positive(message = "{error.idCliente.positive}")
    private final Long idDeposito;

}

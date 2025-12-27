package backend.grupo130.contenedores.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class AsignarClienteRequest {

    private final Long id;

    @NotNull(message = "{error.idCliente.notNull}")
    @Positive(message = "{error.idCliente.positive}")
    private final Long idCliente;

}

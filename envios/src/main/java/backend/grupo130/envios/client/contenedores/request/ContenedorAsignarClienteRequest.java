package backend.grupo130.envios.client.contenedores.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ContenedorAsignarClienteRequest {

    @NotNull(message = "{error.idContenedor.notNull}")
    @Positive(message = "{error.idContenedor.positive}")
    private final Long id;

    @NotNull(message = "{error.idCliente.notNull}")
    @Positive(message = "{error.idCliente.positive}")
    private final Long idCliente;

}

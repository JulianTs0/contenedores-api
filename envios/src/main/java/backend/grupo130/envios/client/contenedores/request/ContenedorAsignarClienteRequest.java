package backend.grupo130.envios.client.contenedores.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ContenedorAsignarClienteRequest {

    private final Long id;

    private final Long idCliente;

}

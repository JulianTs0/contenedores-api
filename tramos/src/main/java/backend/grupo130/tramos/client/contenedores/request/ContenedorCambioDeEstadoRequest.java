package backend.grupo130.tramos.client.contenedores.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class ContenedorCambioDeEstadoRequest {

    @NotNull(message = "{error.idContenedor.notNull}")
    @Positive(message = "{error.idContenedor.positive}")
    private Long id;

    @NotNull(message = "{error.estado.notNull}")
    private String estado;

}


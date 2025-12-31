package backend.grupo130.camiones.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class AsignarTransportistaRequest {

    private final String dominio;

    @NotNull(message = "{error.idTransportista.notNull}")
    @Positive(message = "{error.idTransportista.positive}")
    private final Long idTransportista;

}

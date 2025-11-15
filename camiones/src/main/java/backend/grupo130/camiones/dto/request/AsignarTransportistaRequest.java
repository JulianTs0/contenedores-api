package backend.grupo130.camiones.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AsignarTransportistaRequest {

    @NotBlank(message = "{error.dominio.notBlank}")
    @Size(max = 80, message = "{error.dominio.max}")
    private final String dominio;

    @NotNull(message = "{error.idTransportista.notNull}")
    @Positive(message = "{error.idTransportista.positive}")
    private final Long idTransportista;

}

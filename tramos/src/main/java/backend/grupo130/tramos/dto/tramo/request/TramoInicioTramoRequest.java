package backend.grupo130.tramos.dto.tramo.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class TramoInicioTramoRequest {

    private final Long idTramo;

    @NotBlank(message = "{error.dominioCamion.notBlank}")
    private final String dominioCamion;

}

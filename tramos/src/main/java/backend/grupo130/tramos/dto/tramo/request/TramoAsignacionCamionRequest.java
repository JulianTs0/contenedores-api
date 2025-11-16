package backend.grupo130.tramos.dto.tramo.request;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class TramoAsignacionCamionRequest {

    @NotNull(message = "{error.idTramo.notNull}")
    @Positive(message = "{error.idTramo.positive}")
    private final Long idTramo;

    @NotBlank(message = "{error.dominioCamion.notBlank}")
    private final String dominioCamion;

}

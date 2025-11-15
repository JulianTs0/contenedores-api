package backend.grupo130.contenedores.dto.response;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class GetByPesoVolumenResponse {

    private final Long id;

}

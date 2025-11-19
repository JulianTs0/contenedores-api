package backend.grupo130.contenedores.dto.request;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class GetByPesoVolumenRequest {

    private final BigDecimal peso;

    private final BigDecimal volumen;

}

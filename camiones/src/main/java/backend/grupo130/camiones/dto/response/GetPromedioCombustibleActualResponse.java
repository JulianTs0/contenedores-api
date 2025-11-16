package backend.grupo130.camiones.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class GetPromedioCombustibleActualResponse {

    private final BigDecimal consumoAprox;

}


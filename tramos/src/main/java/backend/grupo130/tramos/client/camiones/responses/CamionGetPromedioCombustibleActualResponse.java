package backend.grupo130.tramos.client.camiones.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
@AllArgsConstructor
public class CamionGetPromedioCombustibleActualResponse {

    private final BigDecimal consumoAprox;

}


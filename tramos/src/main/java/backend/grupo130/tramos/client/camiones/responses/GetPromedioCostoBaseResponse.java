package backend.grupo130.tramos.client.camiones.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class GetPromedioCostoBaseResponse {

    private final BigDecimal promedio;

}

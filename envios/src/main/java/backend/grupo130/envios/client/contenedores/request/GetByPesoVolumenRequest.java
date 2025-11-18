package backend.grupo130.envios.client.contenedores.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class GetByPesoVolumenRequest {

    private final BigDecimal peso;

    private final BigDecimal volumen;

}

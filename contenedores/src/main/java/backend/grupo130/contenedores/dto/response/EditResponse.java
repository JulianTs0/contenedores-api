package backend.grupo130.contenedores.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class EditResponse {

    private final BigDecimal peso;

    private final BigDecimal volumen;

    private final Long idCliente;

}

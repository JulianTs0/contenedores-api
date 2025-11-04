package backend.grupo130.contenedores.dto.response;

import backend.grupo130.contenedores.config.enums.Estado;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class GetByIdResponse {

    private final Integer idContenedor;

    private final BigDecimal peso;

    private final BigDecimal volumen;

    private final Integer idCliente;

    private final String estado;

}

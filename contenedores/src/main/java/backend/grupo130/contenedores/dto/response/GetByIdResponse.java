package backend.grupo130.contenedores.dto.response;

import backend.grupo130.contenedores.client.usuarios.entity.Usuario;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class GetByIdResponse {

    private final Long idContenedor;

    private final BigDecimal peso;

    private final BigDecimal volumen;

    private final Usuario cliente;

    private final String estado;

}

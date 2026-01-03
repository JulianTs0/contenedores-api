package backend.grupo130.tramos.client.contenedores.responses;

import backend.grupo130.tramos.config.entity.Usuario;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
@AllArgsConstructor
public class ContenedorGetByIdResponse {

    private final Long idContenedor;

    private final BigDecimal peso;

    private final BigDecimal volumen;

    private final Usuario cliente;

    private final String estado;

}

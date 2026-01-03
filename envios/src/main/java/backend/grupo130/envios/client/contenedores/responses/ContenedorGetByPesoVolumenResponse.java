package backend.grupo130.envios.client.contenedores.responses;

import backend.grupo130.envios.client.contenedores.entity.Usuario;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class ContenedorGetByPesoVolumenResponse {

    private final Long idContenedor;

    private final BigDecimal peso;

    private final BigDecimal volumen;

    private final Usuario cliente;

    private final String estado;

}

package backend.grupo130.contenedores.dto.response;

import backend.grupo130.contenedores.client.usuarios.entity.Usuario;
import backend.grupo130.contenedores.config.enums.EstadoContenedor;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class AsignarClienteResponse {

    private Long idContenedor;

    private BigDecimal peso;

    private BigDecimal volumen;

    private Usuario cliente;

    private EstadoContenedor estado;

}

package backend.grupo130.envios.client.contenedores.responses;

import backend.grupo130.envios.client.contenedores.entity.Usuario;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ContenedorGetByIdResponse {

    private Long idContenedor;

    private BigDecimal peso;

    private BigDecimal volumen;

    private Usuario cliente;

    private String estado;

}

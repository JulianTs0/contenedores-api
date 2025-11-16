package backend.grupo130.envios.client.contenedores.models;

import backend.grupo130.envios.config.enums.EstadoContenedor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Contenedor {

    private Long idContenedor;

    private BigDecimal peso;

    private BigDecimal volumen;

    private Long idCliente;

    private EstadoContenedor estado;

}

package backend.grupo130.contenedores.data.entity;

import backend.grupo130.contenedores.client.usuarios.entity.Usuario;
import backend.grupo130.contenedores.config.enums.EstadoContenedor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Contenedor {

    private Long idContenedor;

    private BigDecimal peso;

    private BigDecimal volumen;

    private Usuario cliente;

    private EstadoContenedor estado;

    public boolean esBorrador(){
        return this.estado.equals(EstadoContenedor.BORRADOR);
    }

    public boolean esProgramado(){
        return this.estado.equals(EstadoContenedor.PROGRAMADO);
    }

    public boolean esEnTransito(){
        return this.estado.equals(EstadoContenedor.EN_TRANSITO);
    }

    public boolean esEnDeposito(){
        return this.estado.equals(EstadoContenedor.EN_DEPOSITO);
    }

    public boolean esEntregado(){
        return this.estado.equals(EstadoContenedor.ENTREGADO);
    }

}

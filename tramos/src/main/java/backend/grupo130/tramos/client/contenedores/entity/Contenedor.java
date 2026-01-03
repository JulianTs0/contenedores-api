package backend.grupo130.tramos.client.contenedores.entity;

import backend.grupo130.tramos.config.entity.Usuario;
import backend.grupo130.tramos.config.enums.EstadoContenedor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
public class Contenedor {

    private final Long idContenedor;

    private final BigDecimal peso;

    private final BigDecimal volumen;

    private final Usuario cliente;

    private final EstadoContenedor estado;

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

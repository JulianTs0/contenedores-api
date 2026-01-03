package backend.grupo130.tramos.data.entity;

import backend.grupo130.tramos.client.camiones.entity.Camion;
import backend.grupo130.tramos.client.ubicaciones.entity.Ubicacion;
import backend.grupo130.tramos.config.enums.EstadoTramo;
import backend.grupo130.tramos.config.enums.TipoTramo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tramo {

    private Long idTramo;

    private TipoTramo tipoTramo;

    private EstadoTramo estado;

    private BigDecimal costoAproximado;

    private BigDecimal costoReal;

    private LocalDateTime fechaHoraInicioEstimado;

    private LocalDateTime fechaHoraFinEstimado;

    private LocalDateTime fechaHoraInicioReal;

    private LocalDateTime fechaHoraFinReal;

    private Integer orden;

    private Camion camion;

    private RutaTraslado rutaTraslado;

    private Ubicacion origen;

    private Ubicacion destino;

    private Double distancia;

    public boolean esEstimado(){
        return estado.equals(EstadoTramo.ESTIMADO);
    }

    public boolean esAsignado() {
        return  estado.equals(EstadoTramo.ASIGNADO);
    }

    public boolean esIniciado() {
        return  estado.equals(EstadoTramo.INICIADO);
    }

    public boolean esFinalizado(){
        return estado.equals(EstadoTramo.FINALIZADO);
    }

    public boolean esOrigenDeposito(){
        return this.tipoTramo.equals(TipoTramo.ORIGEN_DEPOSITO);
    }
    public boolean esDepositoDeposito(){
        return this.tipoTramo.equals(TipoTramo.DEPOSITO_DEPOSITO);
    }
    public boolean esDepositoDestino() {
        return this.tipoTramo.equals(TipoTramo.DEPOSITO_DESTINO);
    }
    public boolean esOrigenDestino(){
        return this.tipoTramo.equals(TipoTramo.ORIGEN_DESTINO);
    }

    public void resolverTipo(){

        if(this.origen.getDeposito() == null && this.destino.getDeposito() == null){
            this.setTipoTramo(TipoTramo.ORIGEN_DESTINO);
        }
        else if (this.origen.getDeposito() == null){
            this.setTipoTramo(TipoTramo.ORIGEN_DEPOSITO);
        }
        else if (this.destino.getDeposito() == null){
            this.setTipoTramo(TipoTramo.DEPOSITO_DESTINO);
        }
        else {
            this.setTipoTramo(TipoTramo.DEPOSITO_DEPOSITO);
        }

    }
}

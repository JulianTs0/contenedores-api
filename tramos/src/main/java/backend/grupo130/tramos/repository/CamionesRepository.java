package backend.grupo130.tramos.repository;

import backend.grupo130.tramos.client.camiones.CamionClient;
import backend.grupo130.tramos.client.camiones.entity.Camion;
import backend.grupo130.tramos.client.camiones.request.CamionCambiarDisponibilidadRequest;
import backend.grupo130.tramos.client.camiones.responses.*;
import backend.grupo130.tramos.config.enums.Errores;
import backend.grupo130.tramos.config.exceptions.ServiceError;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
@AllArgsConstructor
public class CamionesRepository {

    private final CamionClient camionClient;

    public Camion getById(String camionId){

        try {

            CamionGetCamionByIdResponse response = this.camionClient.getBYId(camionId);

            Camion camion = new Camion(
                response.getDominio(),
                response.getCapacidadPeso(),
                response.getCapacidadVolumen(),
                response.getConsumoCombustible(),
                response.getCostoTrasladoBase(),
                response.getEstado(),
                response.getIdTransportista()
            );

            return camion;

        } catch (ServiceError ex) {
            throw ex;
        } catch (Exception ex){
            throw new ServiceError(ex.getMessage(), Errores.ERROR_INTERNO , 500);
        }

    }

    public void cambiarDisponibilidad(String dominio, Boolean estado){

        try {

            CamionCambiarDisponibilidadRequest request = new CamionCambiarDisponibilidadRequest(dominio, estado);

            CamionEditResponse response = this.camionClient.cambiarDisponibilidad(request);

        } catch (ServiceError ex) {
            throw ex;
        } catch (Exception ex){
            throw new ServiceError(ex.getMessage(), Errores.ERROR_INTERNO , 500);
        }

    }

    public BigDecimal getPromedioCostoBase(BigDecimal peso, BigDecimal volumen){

        try {

            CamionGetPromedioCostoBaseResponse response = this.camionClient.getCostoPromedio(peso, volumen);

            return response.getPromedio();

        } catch (ServiceError ex) {
            throw ex;
        } catch (Exception ex){
            throw new ServiceError(ex.getMessage(), Errores.ERROR_INTERNO , 500);
        }

    }

    public BigDecimal getConsumoPromedio(){

        try {

            CamionGetPromedioCombustibleActualResponse response = this.camionClient.getConsumoPromedio();

            return response.getConsumoAprox();

        } catch (ServiceError ex) {
            throw ex;
        } catch (Exception ex){
            throw new ServiceError(ex.getMessage(), Errores.ERROR_INTERNO , 500);
        }

    }

}

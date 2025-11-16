package backend.grupo130.tramos.repository;

import backend.grupo130.tramos.client.camiones.CamionClient;
import backend.grupo130.tramos.client.camiones.models.Camion;
import backend.grupo130.tramos.client.camiones.request.GetOpcionesCamionesRequest;
import backend.grupo130.tramos.client.camiones.responses.GetAllResponse;
import backend.grupo130.tramos.client.camiones.responses.GetCamionByIdResponse;
import backend.grupo130.tramos.client.camiones.responses.GetPromedioCombustibleActualResponse;
import backend.grupo130.tramos.client.camiones.responses.GetPromedioCostoBaseResponse;
import backend.grupo130.tramos.config.enums.Errores;
import backend.grupo130.tramos.config.exceptions.ServiceError;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
@AllArgsConstructor
public class CamionesRepository {

    private final CamionClient camionClient;

    public Camion getById(String camionId){

        try {

            GetCamionByIdResponse response = this.camionClient.getBYId(camionId);

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

    public BigDecimal getPromedioCostoBase(BigDecimal peso, BigDecimal volumen){

        try {

            GetOpcionesCamionesRequest request = new GetOpcionesCamionesRequest(peso, volumen);

            GetPromedioCostoBaseResponse response = this.camionClient.getPromedioCostoBase(request);

            return response.getPromedio();

        } catch (ServiceError ex) {
            throw ex;
        } catch (Exception ex){
            throw new ServiceError(ex.getMessage(), Errores.ERROR_INTERNO , 500);
        }

    }

    public BigDecimal getConsumoPromedio(){

        try {

            GetPromedioCombustibleActualResponse response = this.camionClient.getConsumoPromedio();

            return response.getConsumoAprox();

        } catch (ServiceError ex) {
            throw ex;
        } catch (Exception ex){
            throw new ServiceError(ex.getMessage(), Errores.ERROR_INTERNO , 500);
        }

    }

}

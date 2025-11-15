package backend.grupo130.tramos.repository;

import backend.grupo130.tramos.client.camiones.CamionClient;
import backend.grupo130.tramos.client.camiones.models.Camion;
import backend.grupo130.tramos.client.camiones.responses.GetCamionByIdResponse;
import backend.grupo130.tramos.config.exceptions.ServiceError;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

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

        } catch (Exception ex){
            throw new ServiceError("Error interno", 500);
        }

    }

}

package backend.grupo130.tramos.client.camiones;

import backend.grupo130.tramos.client.camiones.entity.Camion;
import backend.grupo130.tramos.client.camiones.responses.CamionEditResponse;
import backend.grupo130.tramos.client.camiones.responses.CamionGetCamionByIdResponse;
import backend.grupo130.tramos.client.camiones.responses.CamionGetPromedioCombustibleActualResponse;
import backend.grupo130.tramos.client.camiones.responses.CamionGetPromedioCostoBaseResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Repository
@AllArgsConstructor
public class CamionClient {

    private final CamionGateway camionGateway;

    public Camion getById(String camionId){

        CamionGetCamionByIdResponse response = this.camionGateway.getById(camionId);

        Camion camion = new Camion(
            response.getDominio(),
            response.getCapacidadPeso(),
            response.getCapacidadVolumen(),
            response.getConsumoCombustible(),
            response.getCostoTrasladoBase(),
            response.getEstado(),
            response.getTransportista()
        );

        return camion;
    }

    public CamionEditResponse cambiarDisponibilidad(String dominio, Boolean estado) {

        Map<String, Object> request = new HashMap<>();

        request.put("estado", estado);

        CamionEditResponse response = this.camionGateway.cambiarDisponibilidad(dominio, request);

        return response;
    }

    public BigDecimal getPromedioCostoBase(BigDecimal peso, BigDecimal volumen){

        CamionGetPromedioCostoBaseResponse response = this.camionGateway.getCostoPromedio(peso, volumen);

        return response.getPromedio();
    }

    public BigDecimal getConsumoPromedio(){

        CamionGetPromedioCombustibleActualResponse response = this.camionGateway.getConsumoPromedio();

        return response.getConsumoAprox();
    }

}

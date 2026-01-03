package backend.grupo130.tramos.client.camiones;

import backend.grupo130.tramos.client.camiones.entity.Camion;
import backend.grupo130.tramos.client.camiones.responses.CamionEditResponse;
import backend.grupo130.tramos.client.camiones.responses.CamionGetCamionByIdResponse;
import backend.grupo130.tramos.client.camiones.responses.CamionGetPromedioCombustibleActualResponse;
import backend.grupo130.tramos.client.camiones.responses.CamionGetPromedioCostoBaseResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Component
@AllArgsConstructor
public class CamionClient {

    private final CamionGateway camionGateway;

    public Camion getById(String camionId){

        CamionGetCamionByIdResponse response = this.camionGateway.getById(camionId);

        return new Camion(
            response.getDominio(),
            response.getCapacidadPeso(),
            response.getCapacidadVolumen(),
            response.getConsumoCombustible(),
            response.getCostoTrasladoBase(),
            response.getEstado(),
            response.getTransportista()
        );
    }

    public CamionEditResponse cambiarDisponibilidad(String dominio, Boolean estado) {

        Map<String, Object> request = new HashMap<>();

        request.put("estado", estado);

        return this.camionGateway.cambiarDisponibilidad(dominio, request);
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

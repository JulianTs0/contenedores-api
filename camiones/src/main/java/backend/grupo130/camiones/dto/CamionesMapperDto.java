package backend.grupo130.camiones.dto;

import backend.grupo130.camiones.client.usuarios.entity.Usuario;
import backend.grupo130.camiones.data.models.Camion;
import backend.grupo130.camiones.dto.response.*;

import java.math.BigDecimal;
import java.util.List;

public class CamionesMapperDto {

    public static GetByIdResponse toResponseGet(Camion camion, Usuario transportista) {
        return new GetByIdResponse(
            camion.getDominio(),
            camion.getCapacidadPeso(),
            camion.getCapacidadVolumen(),
            camion.getConsumoCombustible(),
            camion.getCostoTrasladoBase(),
            camion.getEstado(),
            transportista
        );
    }

    public static GetAllResponse toResponseGet(List<Camion> camiones) {
        return new GetAllResponse(camiones);
    }

    public static EditResponse toResponsePatch(Camion camion) {
        return new EditResponse(
            camion.getDominio(),
            camion.getCapacidadPeso(),
            camion.getCapacidadVolumen(),
            camion.getConsumoCombustible(),
            camion.getCostoTrasladoBase(),
            camion.getEstado()
        );
    }

    public static GetPromedioCostoBaseResponse toResponsePromedioCostoBase(BigDecimal promedio){
        return new GetPromedioCostoBaseResponse(
            promedio
        );
    }

    public static GetPromedioCombustibleActualResponse toResponsePromedioCombustible(BigDecimal promedio){
        return new GetPromedioCombustibleActualResponse(
            promedio
        );
    }

}

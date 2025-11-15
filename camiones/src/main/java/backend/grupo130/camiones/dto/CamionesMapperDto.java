package backend.grupo130.camiones.dto;

import backend.grupo130.camiones.client.usuarios.models.Usuario;
import backend.grupo130.camiones.data.models.Camion;
import backend.grupo130.camiones.dto.response.EditResponse;
import backend.grupo130.camiones.dto.response.GetAllResponse;
import backend.grupo130.camiones.dto.response.GetByIdResponse;

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

}

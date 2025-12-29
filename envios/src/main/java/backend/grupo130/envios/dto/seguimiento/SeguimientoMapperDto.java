package backend.grupo130.envios.dto.seguimiento;

import backend.grupo130.envios.data.entity.SeguimientoEnvio;
import backend.grupo130.envios.dto.seguimiento.response.SeguimientoGetAllResponse;
import backend.grupo130.envios.dto.seguimiento.response.SeguimientoGetByIdResponse;

import java.util.List;

public class SeguimientoMapperDto {

    public static SeguimientoGetByIdResponse toSeguimientoGetByIdResponse(SeguimientoEnvio seguimiento) {
        return new SeguimientoGetByIdResponse(
            seguimiento.getIdSeguimiento(),
            seguimiento.getFechaHoraInicio(),
            seguimiento.getFechaHoraFin(),
            seguimiento.getEstado().toString(),
            seguimiento.getDescripcion()
        );
    }

    public static SeguimientoGetAllResponse toSeguimientoGetAllResponse(List<SeguimientoEnvio> seguimientos) {
        return new SeguimientoGetAllResponse(
            seguimientos
        );
    }
}

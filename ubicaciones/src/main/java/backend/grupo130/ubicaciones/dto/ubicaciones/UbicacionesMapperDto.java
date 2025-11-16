package backend.grupo130.ubicaciones.dto.ubicaciones;

import backend.grupo130.ubicaciones.data.models.Ubicacion;
import backend.grupo130.ubicaciones.dto.ubicaciones.response.UbicacionEditResponse;
import backend.grupo130.ubicaciones.dto.ubicaciones.response.UbicacionGetAllResponse;
import backend.grupo130.ubicaciones.dto.ubicaciones.response.UbicacionGetByIdResponse;

import java.util.List;

public class UbicacionesMapperDto {

    public static UbicacionGetByIdResponse toResponseGet(Ubicacion ubicacion) {

        return new UbicacionGetByIdResponse(
            ubicacion.getIdUbicacion(),
            ubicacion.getDireccionTextual(),
            ubicacion.getLatitud(),
            ubicacion.getLongitud(),
            ubicacion.getDeposito()
        );
    }

    public static UbicacionGetAllResponse toResponseGet(List<Ubicacion> ubicaciones) {
        return new UbicacionGetAllResponse(
            ubicaciones
        );
    }

    public static UbicacionEditResponse toResponsePatch(Ubicacion ubicacion) {
        return new UbicacionEditResponse(
            ubicacion.getIdUbicacion(),
            ubicacion.getDireccionTextual(),
            ubicacion.getLatitud(),
            ubicacion.getLongitud()
        );
    }

}

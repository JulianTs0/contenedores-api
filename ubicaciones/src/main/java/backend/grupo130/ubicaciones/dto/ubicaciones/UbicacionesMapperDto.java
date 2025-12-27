
package backend.grupo130.ubicaciones.dto.ubicaciones;

import backend.grupo130.ubicaciones.data.entity.Ubicacion;
import backend.grupo130.ubicaciones.dto.ubicaciones.request.UbicacionEditRequest;
import backend.grupo130.ubicaciones.dto.ubicaciones.response.UbicacionRegisterResponse;
import backend.grupo130.ubicaciones.dto.ubicaciones.response.UbicacionEditResponse;
import backend.grupo130.ubicaciones.dto.ubicaciones.response.UbicacionGetAllResponse;
import backend.grupo130.ubicaciones.dto.ubicaciones.response.UbicacionGetByIdResponse;

import java.util.List;
import java.util.stream.Collectors;

public class UbicacionesMapperDto {

    public static UbicacionGetByIdResponse toResponseGetById(Ubicacion ubicacion) {
        return new UbicacionGetByIdResponse(
            ubicacion.getIdUbicacion(),
            ubicacion.getDireccionTextual(),
            ubicacion.getLatitud(),
            ubicacion.getLongitud(),
            ubicacion.getDeposito()
        );
    }

    public static UbicacionGetAllResponse toResponseGetAll(List<Ubicacion> ubicaciones) {
        return new UbicacionGetAllResponse(
            ubicaciones.stream()
                .map(UbicacionesMapperDto::toResponseGetById)
                .collect(Collectors.toList())
        );
    }

    public static UbicacionEditResponse toResponsePatchEdit(Ubicacion ubicacion) {
        return new UbicacionEditResponse(
            ubicacion.getIdUbicacion(),
            ubicacion.getDireccionTextual(),
            ubicacion.getLatitud(),
            ubicacion.getLongitud()
        );
    }

    public static UbicacionRegisterResponse toResponsePostRegister(Ubicacion ubicacion) {
        return new UbicacionRegisterResponse(ubicacion.getIdUbicacion());
    }

    public static UbicacionEditRequest toRequestPatchEdit(Long id, UbicacionEditRequest body) {
        return UbicacionEditRequest.builder()
                .idUbicacion(id)
                .direccion(body.getDireccion())
                .latitud(body.getLatitud())
                .longitud(body.getLongitud())
                .build();
    }

}

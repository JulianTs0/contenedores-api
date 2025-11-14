package backend.grupo130.ubicaciones.dto.ubicaciones.response;

import backend.grupo130.ubicaciones.data.models.Ubicacion;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class UbicacionGetAllResponse {

    private final List<Ubicacion> ubicaciones;

}

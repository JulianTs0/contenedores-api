package backend.grupo130.tramos.client.ubicaciones.responses;

import backend.grupo130.tramos.client.ubicaciones.entity.Ubicacion;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class GetUbicacionGetAllResponse {

    private final List<Ubicacion> ubicaciones;

}

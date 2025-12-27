package backend.grupo130.ubicaciones.dto.ubicaciones.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class UbicacionGetAllResponse {

    private final List<UbicacionGetByIdResponse> ubicaciones;

}

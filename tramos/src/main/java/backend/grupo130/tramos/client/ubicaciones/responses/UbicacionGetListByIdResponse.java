package backend.grupo130.tramos.client.ubicaciones.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class UbicacionGetListByIdResponse {

    private final List<UbicacionGetByIdResponse> ubicaciones;

}

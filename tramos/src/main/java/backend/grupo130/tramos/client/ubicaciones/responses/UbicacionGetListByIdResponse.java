package backend.grupo130.tramos.client.ubicaciones.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class UbicacionGetListByIdResponse {

    private final List<UbicacionGetByIdResponse> ubicaciones;

}

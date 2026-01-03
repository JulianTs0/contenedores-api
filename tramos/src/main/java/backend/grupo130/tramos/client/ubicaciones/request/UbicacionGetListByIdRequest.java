package backend.grupo130.tramos.client.ubicaciones.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class UbicacionGetListByIdRequest {

    private final List<Long> ids;

}

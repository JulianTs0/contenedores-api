package backend.grupo130.ubicaciones.dto.ubicaciones.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class UbicacionGetListByIdRequest {

    private final List<Long> ids;

}

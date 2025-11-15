package backend.grupo130.tramos.dto.tramo.response;

import backend.grupo130.tramos.data.models.Tramo;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class TramoGetAllResponse {

    private final List<TramoGetByIdResponse> tramos;

}

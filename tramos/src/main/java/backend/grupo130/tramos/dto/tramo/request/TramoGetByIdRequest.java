package backend.grupo130.tramos.dto.tramo.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class TramoGetByIdRequest {

    private final Long idTramo;

}

package backend.grupo130.tramos.dto.tramo.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class TramoGetByRutaIdRequest {

    private final Long idRuta;

}

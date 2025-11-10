package backend.grupo130.tramos.dto.tramo.response;

import backend.grupo130.tramos.data.models.Tramo;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;

@Getter
@AllArgsConstructor
public class TramoRegisterResponse {

    private final Set<Integer> depositos;

    private final Tramo tramo;

}

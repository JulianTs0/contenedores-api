package backend.grupo130.camiones.dto.response;

import backend.grupo130.camiones.data.models.Camion;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class GetAllResponse {

    private final List<Camion> camiones;

}
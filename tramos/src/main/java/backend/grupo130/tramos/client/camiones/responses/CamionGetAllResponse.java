package backend.grupo130.tramos.client.camiones.responses;

import backend.grupo130.tramos.client.camiones.entity.Camion;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class CamionGetAllResponse {

    private final List<Camion> camiones;

}

package backend.grupo130.contenedores.dto.response;

import backend.grupo130.contenedores.data.models.Contenedor;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class GetAllResponse {

    private final List<Contenedor> contenedores;

}

package backend.grupo130.contenedores.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class GetAllResponse {

    private final List<GetByIdResponse> contenedores;

}

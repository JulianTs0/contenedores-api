package backend.grupo130.usuarios.dto.usuarios.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class GetAllResponse {

    private final List<GetByIdResponse> usuarios;

}

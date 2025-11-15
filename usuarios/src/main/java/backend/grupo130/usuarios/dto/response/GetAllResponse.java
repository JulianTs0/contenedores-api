package backend.grupo130.usuarios.dto.response;

import backend.grupo130.usuarios.data.models.Usuario;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class GetAllResponse {

    private final List<GetByIdResponse> usuarios;

}

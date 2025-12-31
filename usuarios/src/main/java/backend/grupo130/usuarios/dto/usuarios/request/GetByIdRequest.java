package backend.grupo130.usuarios.dto.usuarios.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetByIdRequest {

    private final Long usuarioId;

}

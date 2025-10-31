package backend.grupo130.usuarios.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetByIdResponse {

    private final String nombre;

    private final String apellido;

    private final String telefono;

    private final String email;

    private final String rol;

}

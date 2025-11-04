package backend.grupo130.contenedores.client.usuarios.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetUserByIdResponse {

    private final String nombre;

    private final String apellido;

    private final String telefono;

    private final String email;

    private final String rol;

}

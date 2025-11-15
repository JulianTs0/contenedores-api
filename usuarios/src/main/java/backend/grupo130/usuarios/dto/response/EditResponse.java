package backend.grupo130.usuarios.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class  EditResponse {

    private final Long id;

    private final String nombre;

    private final String apellido;

    private final String telefono;

    private final String email;

}

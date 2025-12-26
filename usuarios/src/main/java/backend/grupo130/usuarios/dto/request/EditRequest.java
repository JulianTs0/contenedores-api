package backend.grupo130.usuarios.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class EditRequest {

    private final Long id;

    @Size(max = 30, message = "{error.nombre.max}")
    @Size(min = 1, message = "{error.nombre.notBlank}")
    private String nombre;

    @Size(max = 30, message = "{error.apellido.max}")
    @Size(min = 1, message = "{error.apellido.notBlank}")
    private final String apellido;

    @Size(max = 30, message = "{error.telefono.max}")
    @Size(min = 1, message = "{error.telefono.notBlank}")
    private final String telefono;

    @Email(message = "{error.email.format}")
    @Size(max = 30, message = "{error.email.max}")
    @Size(min = 1, message = "{error.email.notBlank}")
    private final String email;

}

package backend.grupo130.usuarios.dto.usuarios.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "{error.nombre.notBlank}")
    @Size(max = 30, message = "{error.nombre.max}")
    private final String nombre;

    @NotBlank(message = "{error.apellido.notBlank}")
    @Size(max = 30, message = "{error.apellido.max}")
    private final String apellido;

    @Size(max = 30, message = "{error.telefono.max}")
    @Size(min = 1, message = "{error.telefono.notBlank}")
    private final String telefono;

    @NotBlank(message = "{error.email.notBlank}")
    @Email(message = "{error.email.format}")
    @Size(max = 60, message = "{error.email.max}")
    private final String email;

    @NotBlank(message = "{error.password.notBlank}")
    @Size(min = 6, message = "{error.password.min}")
    private final String password;

    @NotEmpty(message = "{error.rol.notBlank}")
    private final List<String> roles;

}

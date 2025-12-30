package backend.grupo130.usuarios.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "{error.nombre.notBlank}")
    @Size(max = 30, message = "{error.nombre.max}")
    private String nombre;

    @NotBlank(message = "{error.apellido.notBlank}")
    @Size(max = 30, message = "{error.apellido.max}")
    private String apellido;

    @Size(max = 30, message = "{error.telefono.max}")
    private String telefono;

    @NotBlank(message = "{error.email.notBlank}")
    @Email(message = "{error.email.format}")
    @Size(max = 60, message = "{error.email.max}")
    private String email;

    private List<String> roles;

}

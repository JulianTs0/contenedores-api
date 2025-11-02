package backend.grupo130.usuarios.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RegisterRequest {

    @NotNull(message = "El nombre es obligatorio")
    @NotBlank(message = "El nombre no puede estar vacio")
    @Size(max = 30, message = "El nombre no puede exceder los 30 caracteres")
    private String nombre;

    @NotNull(message = "El apellido es obligatorio")
    @NotBlank(message = "El apellido no puede estar vacio")
    @Size(max = 30, message = "El apellido no puede exceder los 30 caracteres")
    private String apellido;

    @Size(max = 30, message = "El teléfono no puede exceder los 30 caracteres")
    private String telefono;

    @NotNull(message = "El email es obligatorio")
    @NotBlank(message = "El email no puede estar vacio")
    @Email(message = "Formato de email inválido")
    @Size(max = 60, message = "El email no puede exceder los 60 caracteres")
    private String email;

    @NotNull(message = "El rol es obligatorio")
    @NotBlank(message = "El rol es obligatorio")
    private String rol;

}

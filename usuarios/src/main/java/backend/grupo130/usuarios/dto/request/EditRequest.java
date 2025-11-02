package backend.grupo130.usuarios.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EditRequest {

    @NotNull(message = "La id es obligatoria")
    @Min(value = 1, message = "El ID de usuario debe ser un número positivo")
    private final Integer id;

    @Size(max = 30, message = "El nombre no puede exceder los 30 caracteres")
    @Size(min = 1, message = "El email no puede estar vacio")
    private String nombre;

    @Size(max = 30, message = "El apellido no puede exceder los 30 caracteres")
    @Size(min = 1, message = "El email no puede estar vacio")
    private final String apellido;

    @Size(max = 30, message = "El teléfono no puede exceder los 30 caracteres")
    @Size(min = 1, message = "El email no puede estar vacio")
    private final String telefono;

    @Email(message = "Formato de email inválido")
    @Size(max = 60, message = "El email no puede exceder los 60 caracteres")
    @Size(min = 1, message = "El email no puede estar vacio")
    private final String email;

}

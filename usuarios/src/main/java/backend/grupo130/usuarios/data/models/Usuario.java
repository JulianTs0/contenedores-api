package backend.grupo130.usuarios.data.models;

import backend.grupo130.usuarios.config.enums.Rol;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "clientes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cliente")
    private Integer idUsuario;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 30, message = "El nombre no puede exceder los 30 caracteres")

    @Column(name = "nombre", length = 30, nullable = false)
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    @Size(max = 30, message = "El apellido no puede exceder los 30 caracteres")

    @Column(name = "apellido", length = 30, nullable = false)
    private String apellido;

    @Size(max = 30, message = "El teléfono no puede exceder los 30 caracteres")

    @Column(name = "telefono", length = 30)
    private String telefono;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Formato de email inválido")

    @Size(max = 60, message = "El email no puede exceder los 60 caracteres")
    @Column(name = "email", length = 60, nullable = false)
    private String email;

    @NotNull(message = "El rol es obligatorio")

    @Enumerated(EnumType.STRING)
    @Column(name = "rol", length = 20, nullable = false)
    private Rol rol;
}

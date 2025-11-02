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

    @Column(name = "nombre", length = 30, nullable = false)
    private String nombre;

    @Column(name = "apellido", length = 30, nullable = false)
    private String apellido;

    @Column(name = "telefono", length = 30)
    private String telefono;

    @Column(name = "email", length = 60, nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "rol", length = 20, nullable = false)
    private Rol rol;
}

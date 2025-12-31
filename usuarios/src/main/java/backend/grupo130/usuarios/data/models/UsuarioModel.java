package backend.grupo130.usuarios.data.models;

import backend.grupo130.usuarios.config.enums.Rol;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;


@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long idUsuario;

    @Column(name = "keycloak_id", unique = true, nullable = false)
    private String keycloakId;

    @Column(length = 30, nullable = false)
    private String nombre;

    @Column(length = 30, nullable = false)
    private String apellido;

    @Column(length = 30)
    private String telefono;

    @Column(length = 60, nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private Set<Rol> roles;

}

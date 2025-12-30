package backend.grupo130.contenedores.client.usuarios.entity;

import backend.grupo130.contenedores.config.enums.Rol;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    private Long idUsuario;

    private String nombre;

    private String apellido;

    private String telefono;

    private String email;

    private Set<Rol> roles;

}

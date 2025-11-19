package backend.grupo130.contenedores.client.usuarios.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    private Long idUsuario;

    private String nombre;

    private String apellido;

    private String telefono;

    private String email;

    private String rol;

}

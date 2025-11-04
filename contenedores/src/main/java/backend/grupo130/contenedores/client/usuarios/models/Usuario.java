package backend.grupo130.contenedores.client.usuarios.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    private Integer idUsuario;

    private String nombre;

    private String apellido;

    private String telefono;

    private String email;

    private String rol;

}

package backend.grupo130.envios.client.contenedores.entity;

import backend.grupo130.envios.config.enums.Rol;
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

    private Rol rol;

}

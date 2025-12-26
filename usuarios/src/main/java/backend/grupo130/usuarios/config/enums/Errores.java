package backend.grupo130.usuarios.config.enums;

import lombok.Getter;

@Getter
public enum Errores {

    USUARIO_NO_ENCONTRADO("UsuarioModel no encontrado"),
    ROL_INVALIDO("El rol es invalido"),
    ERROR_INTERNO("Error interno");

    private final String mensaje;

    Errores(String mensaje) {
        this.mensaje = mensaje;
    }

}

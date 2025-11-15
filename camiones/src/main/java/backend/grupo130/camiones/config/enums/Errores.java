package backend.grupo130.camiones.config.enums;

import lombok.Getter;

@Getter
public enum Errores {

    CAMION_NO_ENCONTRADO("Camión no encontrado"),
    CAMION_NO_DISPONIBLE("Camión no disponible"),
    USUARIO_NO_TRANSPORTISTA("El usuario debe ser un Transportista"),
    ERROR_INTERNO("Error interno");

    private final String mensaje;

    Errores(String mensaje) {
        this.mensaje = mensaje;
    }

}

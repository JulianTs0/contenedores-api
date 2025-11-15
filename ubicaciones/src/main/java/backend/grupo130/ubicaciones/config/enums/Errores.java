package backend.grupo130.ubicaciones.config.enums;

import lombok.Getter;

@Getter
public enum Errores {

    ERROR_INTERNO("Error interno"),

    UBICACION_NO_ENCONTRADA("Ubicacion no encontrado"),
    DEPOSITO_NO_ENCONTRADO("Deposito no encontrado");

    private final String mensaje;

    Errores(String mensaje) {
        this.mensaje = mensaje;
    }


}

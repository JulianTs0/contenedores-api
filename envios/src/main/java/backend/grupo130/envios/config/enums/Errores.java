package backend.grupo130.envios.config.enums;

import lombok.Getter;

@Getter
public enum Errores {

    ERROR_INTERNO("Error interno"),
    TARIFA_NO_ENCONTRADA("La tarifa solicitada no fue encontrada"),
    DATOS_INVALIDOS("Los datos proporcionados son inválidos");

    private final String mensaje;

    Errores(String mensaje) {
        this.mensaje = mensaje;
    }

}

package backend.grupo130.envios.config.enums;

import lombok.Getter;

@Getter
public enum Errores {

    ERROR_INTERNO("Error interno"),

    ESTADO_INVALIDO("El estado no es valido"),
    TRANSICION_ESTADO_INVALIDA("La transicion de estado no es valida"),

    SOLICITUD_NO_ENCONTRADA("Solicitud no encontrada"),
    SOLICITUD_YA_FINALIZADA("La solicitud ya ha finalizado y no puede cambiar de estado"),
    PRECIO_NO_ENCONTRADO("Precios no encontrados");

    private final String mensaje;

    Errores(String mensaje) {
        this.mensaje = mensaje;
    }

}

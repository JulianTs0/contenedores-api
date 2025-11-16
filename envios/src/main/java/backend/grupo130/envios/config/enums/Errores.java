package backend.grupo130.envios.config.enums;

import lombok.Getter;

@Getter
public enum Errores {

    ERROR_INTERNO("Error interno"),
    TARIFA_NO_ENCONTRADA("La tarifa solicitada no fue encontrada"),
    DATOS_INVALIDOS("Los datos proporcionados son inválidos"),

    SEGUIMIENTO_NO_ENCONTRADO("Seguimiento no encontrado"),
    ESTADO_INVALIDO("El estado no es valido"),
    TRANSICION_ESTADO_INVALIDA("La transicion de estado no es valida"),
    SEGUIMIENTO_YA_FINALIZADO("El seguimiento ya ha finalizado y no puede cambiar de estado"),

    SOLICITUD_NO_ENCONTRADA("Solicitud no encontrada"),
    SOLICITUD_YA_FINALIZADA("La solicitud ya ha finalizado y no puede cambiar de estado"),
    CONTENEDOR_NO_ENCONTRADO("Contenedor no encontrado");

    private final String mensaje;

    Errores(String mensaje) {
        this.mensaje = mensaje;
    }

}

package backend.grupo130.tramos.config.enums;

import lombok.ToString;

@ToString
public enum EstadoTramo {

    ESTIMADO,
    ASIGNADO,
    INICIADO,
    FINALIZADO;

    public static EstadoTramo fromString(String text) {
        for (EstadoTramo b : EstadoTramo.values()) {
            if (b.name().equalsIgnoreCase(text)) {
                return b;
            }
        }
        return null;
    }
}

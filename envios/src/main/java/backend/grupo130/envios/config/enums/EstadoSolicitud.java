package backend.grupo130.envios.config.enums;

import lombok.ToString;

public enum EstadoSolicitud {
    BORRADOR,
    PROGRAMADO,
    EN_TRANSITO,
    ENTREGADO;

    public static EstadoSolicitud fromString(String text) {
        for (EstadoSolicitud b : EstadoSolicitud.values()) {
            if (b.name().equalsIgnoreCase(text)) {
                return b;
            }
        }
        return null;
    }
}

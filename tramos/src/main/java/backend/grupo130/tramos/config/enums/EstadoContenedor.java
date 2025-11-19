package backend.grupo130.tramos.config.enums;

import lombok.ToString;

public enum EstadoContenedor {
    BORRADOR,
    PROGRAMADO,
    EN_TRANSITO,
    EN_DEPOSITO,
    ENTREGADO;

    public static EstadoContenedor fromString(String text) {
        for (EstadoContenedor b : EstadoContenedor.values()) {
            if (b.name().equalsIgnoreCase(text)) {
                return b;
            }
        }
        return null;
    }
}

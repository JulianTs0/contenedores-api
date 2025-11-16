package backend.grupo130.envios.config.enums;

import lombok.ToString;

@ToString
public enum Estado {
    BORRADOR,
    PROGRAMADO,
    EN_TRANSITO,
    ENTREGADO;

    public static Estado fromString(String text) {
        for (Estado b : Estado.values()) {
            if (b.name().equalsIgnoreCase(text)) {
                return b;
            }
        }
        return null;
    }
}

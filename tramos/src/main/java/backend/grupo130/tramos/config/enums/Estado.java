package backend.grupo130.tramos.config.enums;

import lombok.ToString;

@ToString
public enum Estado {
    ESTIMADO,
    ASIGNADO,
    INICIADO,
    FINALZADO;

    public static Estado fromString(String text) {
        for (Estado b : Estado.values()) {
            if (b.name().equalsIgnoreCase(text)) {
                return b;
            }
        }
        return null;
    }
}

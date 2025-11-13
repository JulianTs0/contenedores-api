package backend.grupo130.tramos.config.enums;

import lombok.ToString;

@ToString
public enum Estado {

    // Tramo
    ESTIMADO,
    ASIGNADO,
    INICIADO,
    FINALZADO,

    // Camion
    LIBRE,
    OCUPADO,

    // Solicitud
    BORRADOR,
    PROGRAMADA,
    ENTRANSITO,
    ENTREGADA;

    public static Estado fromString(String text) {
        for (Estado b : Estado.values()) {
            if (b.name().equalsIgnoreCase(text)) {
                return b;
            }
        }
        return null;
    }
}

package backend.grupo130.envios.config.enums;

public enum EstadoSolicitud {
    BORRADOR,
    CONFIRMADA,
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

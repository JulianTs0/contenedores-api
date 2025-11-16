package backend.grupo130.tramos.config.enums;

public enum EstadoSolicitud {

    BORRADOR,
    PROGRAMADO,
    EN_TRANSITO,
    ENTREGADO;

    public static EstadoSolicitud fromString(String text) {
        for (EstadoSolicitud e : EstadoSolicitud.values()) {
            if (e.name().equalsIgnoreCase(text)) {
                return e;
            }
        }
        return null;
    }

}

package backend.grupo130.tramos.config.enums;

public enum TipoTramo {

    ORIGEN_DEPOSITO,
    DEPOSITO_DEPOSITO,
    DEPOSITO_DESTINO,
    ORIGEN_DESTINO;

    public static TipoTramo fromString(String text) {
        for (TipoTramo b : TipoTramo.values()) {
            if (b.name().equalsIgnoreCase(text)) {
                return b;
            }
        }
        return null;
    }
}

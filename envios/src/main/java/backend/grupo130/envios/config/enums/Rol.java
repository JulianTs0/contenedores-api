package backend.grupo130.envios.config.enums;

import lombok.ToString;

@ToString
public enum Rol {
    CLIENTE,
    ADMINISTRADOR,
    TRANSPORTISTA;

    public static Rol fromString(String text) {
        for (Rol b : Rol.values()) {
            if (b.name().equalsIgnoreCase(text)) {
                return b;
            }
        }
        return null;
    }

}

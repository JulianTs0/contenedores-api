package backend.grupo130.tramos.config.enums;

import lombok.ToString;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

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

    public static Set<Rol> fromString(List<String> roles){
        return roles.stream()
            .map(Rol::fromString)
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());
    }

}

package backend.grupo130.contenedores.config.enums;

import lombok.Getter;

@Getter
public enum Errores {

    ERROR_INTERNO("Error interno"),

    CONTENEDOR_NO_ENCONTRADO("Contenedor no encontrado"),
    ESTADO_INVALIDO("El estado no es valido"),
    TRANSICION_ESTADO_INVALIDA("Transicion no valida"),
    CONTENEDOR_YA_ENTREGADO("El container ya fue entregado"),
    USUARIO_NO_ENCONTRADO("Cliente no encontrado"),
    USUARIO_YA_ASIGNADO("El Cliente ya ah sido asignado"),
    USUARIO_NO_CLIENTE("El usuario debe ser un Cliente");

    private final String mensaje;

    Errores(String mensaje) {
        this.mensaje = mensaje;
    }

}

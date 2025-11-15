package backend.grupo130.usuarios.config.exceptions;

import backend.grupo130.usuarios.config.enums.Errores;
import lombok.Getter;

@Getter
public class ServiceError extends RuntimeException {

    private final int httpCode;

    private final String mensajeExterno;

    public ServiceError(String mensajeInterno, Errores error, Integer httpcode) {
        super(mensajeInterno);
        this.httpCode = httpcode;
        this.mensajeExterno = error.getMensaje();
    }
}

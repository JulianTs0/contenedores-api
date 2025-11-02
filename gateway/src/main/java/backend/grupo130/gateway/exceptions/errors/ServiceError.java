package backend.grupo130.gateway.exceptions.errors;

import lombok.Getter;

@Getter
public class ServiceError extends RuntimeException {

    private final int httpCode;

    public ServiceError(String message, Integer httpcode) {
        super(message);
        this.httpCode = httpcode;
    }

}

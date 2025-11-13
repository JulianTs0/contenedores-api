
package backend.grupo130.camiones.config.exceptions;

public class ServiceError extends RuntimeException {
    public ServiceError(String message) {
        super(message);
    }
}

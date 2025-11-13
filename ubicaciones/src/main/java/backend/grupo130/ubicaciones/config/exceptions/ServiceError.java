
package backend.grupo130.ubicaciones.config.exceptions;

public class ServiceError extends RuntimeException {
    public ServiceError(String message) {
        super(message);
    }
}

package backend.grupo130.envios.client.contenedores;

import backend.grupo130.envios.client.contenedores.ContenedorGateway;
import backend.grupo130.envios.client.contenedores.entity.Contenedor;
import backend.grupo130.envios.client.contenedores.entity.Usuario;
import backend.grupo130.envios.client.contenedores.request.ContenedorAsignarClienteRequest;
import backend.grupo130.envios.client.contenedores.request.ContenedorRegisterRequest;
import backend.grupo130.envios.client.contenedores.responses.ContenedorGetByIdResponse;
import backend.grupo130.envios.client.contenedores.responses.ContenedorGetByPesoVolumenResponse;
import backend.grupo130.envios.client.contenedores.responses.ContenedorRegisterResponse;
import backend.grupo130.envios.config.enums.Errores;
import backend.grupo130.envios.config.enums.EstadoContenedor;
import backend.grupo130.envios.config.exceptions.ServiceError;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@AllArgsConstructor
@Slf4j
public class ContenedorClient {

    private final ContenedorGateway contenedorGateway;

    public Contenedor getById(Long id){
            ContenedorGetByIdResponse response = this.contenedorGateway.getBYId(id);

            return new Contenedor(
                response.getIdContenedor(),
                response.getPeso(),
                response.getVolumen(),
                response.getCliente(),
                EstadoContenedor.fromString(response.getEstado())
            );
    }

    public Contenedor getByPesoVolumen(BigDecimal peso, BigDecimal volumen){
        try {

            ContenedorGetByPesoVolumenResponse response = this.contenedorGateway.getByPesoVolumen(peso, volumen);

            return this.getById(response.getId());

        } catch (ServiceError ex) {
            if (ex.getMessage() != null && ex.getMessage().contains("[404]")) {
                 throw new ServiceError("Contenedor no encontrado", Errores.CONTENEDOR_NO_ENCONTRADO, 404);
            }
            throw ex;
        } catch (Exception ex){
            throw new ServiceError(ex.getMessage(), Errores.ERROR_INTERNO , 500);
        }
    }

    public Long register(BigDecimal peso, BigDecimal volumen, Long idCliente){

            ContenedorRegisterRequest request = new ContenedorRegisterRequest(peso, volumen, idCliente);

            ContenedorRegisterResponse response = this.contenedorGateway.register(request);
            
            return response.getId();
    }

    public void asignarCliente(Long idContenedor, Long idCliente) throws ServiceError {
        try {
            log.info("Asignando cliente {} al contenedor {}...", idCliente, idContenedor);
            ContenedorAsignarClienteRequest request = new ContenedorAsignarClienteRequest(idContenedor, idCliente);
            ResponseEntity<Void> response = this.contenedorGateway.asignarCliente(idContenedor, request);

            if (!response.getStatusCode().is2xxSuccessful()) {
                log.error("Error de la API de contenedores al asignar cliente. Status: {}", response.getStatusCode());
                throw new ServiceError("Error al asignar cliente al contenedor", Errores.ERROR_INTERNO, response.getStatusCode().value());
            }
            log.info("Cliente asignado correctamente.");

        } catch (ServiceError ex) {
            if (ex.getMessage() != null && ex.getMessage().contains("[404]")) {
                log.warn("API de contenedores no encontró el contenedor {} para asignar cliente.", idContenedor);
                throw new ServiceError("Contenedor no encontrado al asignar cliente", Errores.CONTENEDOR_NO_ENCONTRADO, 404);
            }
            if (ex.getMessage() != null && ex.getMessage().contains("[400]")) {
                log.warn("API de contenedores rechazó la asignación (400). Cliente: {}, Contenedor: {}", idCliente, idContenedor);
                throw new ServiceError("Error de validacion al asignar cliente (e.g., ya asignado o cliente no válido)", Errores.ERROR_INTERNO, 400);
            }
            throw ex;
        } catch (Exception ex){
            log.error("Error inesperado al asignar cliente: {}", ex.getMessage(), ex);
            throw new ServiceError(ex.getMessage(), Errores.ERROR_INTERNO , 500);
        }
    }

}

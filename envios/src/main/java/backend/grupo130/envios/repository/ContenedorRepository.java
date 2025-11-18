package backend.grupo130.envios.repository;

import backend.grupo130.envios.client.contenedores.ContenedorClient;
import backend.grupo130.envios.client.contenedores.models.Contenedor;
import backend.grupo130.envios.client.contenedores.request.ContenedorAsignarClienteRequest;
import backend.grupo130.envios.client.contenedores.request.ContenedorRegisterRequest;
import backend.grupo130.envios.client.contenedores.request.GetByPesoVolumenRequest;
import backend.grupo130.envios.client.contenedores.responses.ContenedorGetByIdResponse;
import backend.grupo130.envios.client.contenedores.responses.ContenedorGetByPesoVolumenResponse;
import backend.grupo130.envios.config.enums.Errores;
import backend.grupo130.envios.config.enums.EstadoContenedor;
import backend.grupo130.envios.config.exceptions.ServiceError;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
@AllArgsConstructor
@Slf4j
public class ContenedorRepository {

    private final ContenedorClient contenedorClient;

    public Contenedor getById(Long id){
        try {
            ContenedorGetByIdResponse response = this.contenedorClient.getBYId(id);

            Long idCliente = null;

            if (response.getCliente() != null){
                idCliente = response.getCliente().getIdUsuario();
            }

            return new Contenedor(
                response.getIdContenedor(),
                response.getPeso(),
                response.getVolumen(),
                idCliente,
                EstadoContenedor.fromString(response.getEstado())
            );
        } catch (ServiceError ex) {
            throw ex;
        } catch (Exception ex){
            throw new ServiceError(ex.getMessage(), Errores.ERROR_INTERNO , 500);
        }
    }

    public Contenedor getByPesoVolumen(BigDecimal peso, BigDecimal volumen, Long idCliente){
        try {
            GetByPesoVolumenRequest request = new GetByPesoVolumenRequest(peso, volumen, idCliente);
            ContenedorGetByPesoVolumenResponse response = this.contenedorClient.getByPesoVolumen(request);

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

    public void register(BigDecimal peso, BigDecimal volumen, Long idCliente){
        try {
            ContenedorRegisterRequest request = new ContenedorRegisterRequest(peso, volumen, idCliente);
            ResponseEntity<Void> response = this.contenedorClient.register(request);
            
            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new ServiceError("Error al registrar contenedor", Errores.ERROR_INTERNO, response.getStatusCode().value());
            }
        } catch (Exception ex){
            throw new ServiceError(ex.getMessage(), Errores.ERROR_INTERNO , 500);
        }
    }

    public void asignarCliente(Long idContenedor, Long idCliente) throws ServiceError {
        try {
            log.info("Asignando cliente {} al contenedor {}...", idCliente, idContenedor);
            ContenedorAsignarClienteRequest request = new ContenedorAsignarClienteRequest(idContenedor, idCliente);
            ResponseEntity<Void> response = this.contenedorClient.asignarCliente(request);

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

package backend.grupo130.tramos.repository;

import backend.grupo130.tramos.client.contenedores.ContenedorClient;
import backend.grupo130.tramos.client.contenedores.entity.Contenedor;
import backend.grupo130.tramos.client.contenedores.request.ContenedorCambioDeEstadoRequest;
import backend.grupo130.tramos.client.contenedores.responses.ContenedorCambioDeEstadoResponse;
import backend.grupo130.tramos.client.contenedores.responses.ContenedorGetByIdResponse;
import backend.grupo130.tramos.config.enums.Errores;
import backend.grupo130.tramos.config.enums.EstadoContenedor;
import backend.grupo130.tramos.config.exceptions.ServiceError;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class ContenedorRepository {

    private final ContenedorClient contenedorClient;

    public Contenedor getById(Long id){

        try {

            ContenedorGetByIdResponse response = this.contenedorClient.getBYId(id);

            Contenedor contenedor = new Contenedor(
                response.getIdContenedor(),
                response.getPeso(),
                response.getVolumen(),
                response.getCliente().getIdUsuario(),
                EstadoContenedor.fromString(response.getEstado())
            );

            return contenedor;

        } catch (ServiceError ex) {
            throw ex;
        } catch (Exception ex){
            throw new ServiceError(ex.getMessage(), Errores.ERROR_INTERNO , 500);
        }

    }

    public ContenedorCambioDeEstadoResponse cambioDeEstado(Long idContenedor, String nuevoEstado) {
        try {
            ContenedorCambioDeEstadoRequest request = new ContenedorCambioDeEstadoRequest(idContenedor, nuevoEstado);

            return this.contenedorClient.cambioDeEstado(request);
        } catch (ServiceError ex) {
            throw ex;
        } catch (Exception ex){
            throw new ServiceError(ex.getMessage(), Errores.ERROR_INTERNO , 500);
        }
    }

}

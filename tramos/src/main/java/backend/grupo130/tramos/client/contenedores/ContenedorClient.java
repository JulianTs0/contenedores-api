package backend.grupo130.tramos.client.contenedores;

import backend.grupo130.tramos.client.contenedores.entity.Contenedor;
import backend.grupo130.tramos.client.contenedores.responses.ContenedorCambioDeEstadoResponse;
import backend.grupo130.tramos.client.contenedores.responses.ContenedorGetByIdResponse;
import backend.grupo130.tramos.config.enums.EstadoContenedor;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Component
@AllArgsConstructor
public class ContenedorClient {

    private final ContenedorGateway contenedorGateway;

    public Contenedor getById(Long id){

        ContenedorGetByIdResponse response = this.contenedorGateway.getById(id);

        return new Contenedor(
            response.getIdContenedor(),
            response.getPeso(),
            response.getVolumen(),
            response.getCliente(),
            EstadoContenedor.fromString(response.getEstado())
        );

    }

    public ContenedorCambioDeEstadoResponse cambioDeEstado(Long idContenedor, String nuevoEstado) {

        Map<String, Object> request = new HashMap<>();

        request.put("estado", nuevoEstado);

        return this.contenedorGateway.cambioDeEstado(idContenedor, request);
    }

}

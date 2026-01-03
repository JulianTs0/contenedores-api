package backend.grupo130.envios.client.contenedores;

import backend.grupo130.envios.client.contenedores.entity.Contenedor;
import backend.grupo130.envios.client.contenedores.request.ContenedorAsignarClienteRequest;
import backend.grupo130.envios.client.contenedores.request.ContenedorRegisterRequest;
import backend.grupo130.envios.client.contenedores.responses.ContenedorGetByIdResponse;
import backend.grupo130.envios.client.contenedores.responses.ContenedorGetByPesoVolumenResponse;
import backend.grupo130.envios.client.contenedores.responses.ContenedorRegisterResponse;
import backend.grupo130.envios.config.enums.EstadoContenedor;
import backend.grupo130.envios.config.exceptions.ServiceError;
import feign.FeignException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

import static net.logstash.logback.argument.StructuredArguments.kv;

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

            Contenedor contenedor = new Contenedor();

            contenedor.setIdContenedor(response.getIdContenedor());
            contenedor.setVolumen(response.getVolumen());
            contenedor.setPeso(response.getPeso());
            contenedor.setCliente(response.getCliente());
            contenedor.setEstado(EstadoContenedor.fromString(response.getEstado()));

            return contenedor;
        } catch (FeignException ex) {
            if (ex.status() == 404) {
                return null;
            }
            throw ex;
        }
    }

    public Long register(BigDecimal peso, BigDecimal volumen, Long idCliente){

            ContenedorRegisterRequest request = new ContenedorRegisterRequest(peso, volumen, idCliente);

            ContenedorRegisterResponse response = this.contenedorGateway.register(request);

            return response.getId();
    }

    public void asignarCliente(Long idContenedor, Long idCliente) throws ServiceError {
            log.info("Asignando cliente al contenedor",
                    kv("evento", "asignar_cliente_contenedor"),
                    kv("id_cliente", idCliente),
                    kv("id_contenedor", idContenedor)
            );

            ContenedorAsignarClienteRequest request = new ContenedorAsignarClienteRequest(idContenedor, idCliente);

            this.contenedorGateway.asignarCliente(idContenedor, request);

            log.info("Cliente asignado correctamente",
                    kv("evento", "cliente_asignado_contenedor"),
                    kv("id_cliente", idCliente),
                    kv("id_contenedor", idContenedor)
            );
    }

}

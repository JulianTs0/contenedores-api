package backend.grupo130.tramos.service;

import backend.grupo130.tramos.client.camiones.models.Camion;
import backend.grupo130.tramos.client.ubicaciones.models.Ubicacion;
import backend.grupo130.tramos.config.enums.Estado;
import backend.grupo130.tramos.config.exceptions.ServiceError;
import backend.grupo130.tramos.data.models.RutaTraslado;
import backend.grupo130.tramos.data.models.Tramo;
import backend.grupo130.tramos.dto.tramo.TramoMapperDto;
import backend.grupo130.tramos.dto.tramo.request.*;
import backend.grupo130.tramos.dto.tramo.response.TramoGetAllResponse;
import backend.grupo130.tramos.dto.tramo.response.TramoGetByIdResponse;
import backend.grupo130.tramos.repository.*;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class TramoService {

    private final TramoRepository tramoRepository;

    private final RutaRepository rutaRepository;

    private final CamionesRepository camionesRepository;

    private final UbicacionesRepository ubicacionesRepository;

    // private final EnviosRepository enviosRepository;

    public TramoGetByIdResponse getById(TramoGetByIdRequest request) throws ServiceError {
        try {

            Tramo tramo = this.tramoRepository.getById(request.getIdTramo());

            if (tramo == null) {
                throw new ServiceError("Tramo no encontrado", 404);
            }

            // Camion camion =  this.camionesRepository.getById(tramo.getDominioCamion());
            // tramo.setCamion(camion);

            Ubicacion origen = this.ubicacionesRepository.getUbicacionById(tramo.getIdOrigen());
            Ubicacion destino = this.ubicacionesRepository.getUbicacionById(tramo.getIdDestino());
            tramo.setOrigen(origen);
            tramo.setDestino(destino);

            TramoGetByIdResponse response = TramoMapperDto.toResponseGet(tramo);

            return response;
        } catch (ServiceError ex) {
            throw ex;
        }
        catch (Exception ex) {
            log.warn(ex.getMessage());
            ex.printStackTrace();
            throw new ServiceError("Error interno", 500);
        }
    }

    public TramoGetAllResponse getByTransportista(TramoGetByTransportistaRequest request) throws ServiceError {
        try {

            List<Tramo> tramos = this.tramoRepository.getAll()
                .stream()
                .filter(tramo -> tramo.getDominioCamion().equals(request.getDominioCamion()))
                .toList();

            TramoGetAllResponse response = TramoMapperDto.toResponseGet(tramos);

            return response;
        } catch (ServiceError ex) {
            throw ex;
        }  catch (Exception ex) {
            log.warn(ex.getMessage());
            ex.printStackTrace();
            throw new ServiceError("Error interno", 500);
        }
    }

    public TramoGetAllResponse getTramosDeRuta(TramoGetByRutaIdRequest request) throws ServiceError {
        try {

            List<Tramo> tramosDeLaRuta = this.tramoRepository.buscarPorRuta(request.getIdRuta());

            TramoGetAllResponse response = TramoMapperDto.toResponseGet(tramosDeLaRuta);

            return response;
        } catch (ServiceError ex) {
            throw ex;
        }  catch (Exception ex) {
            log.warn(ex.getMessage());
            ex.printStackTrace();
            throw new ServiceError("Error interno", 500);
        }
    }

    public TramoGetAllResponse getAll() throws ServiceError {
        try {

            List<Tramo> tramos = this.tramoRepository.getAll();

            TramoGetAllResponse response = TramoMapperDto.toResponseGet(tramos);

            return response;
        } catch (ServiceError ex) {
            throw ex;
        }  catch (Exception ex) {
            log.warn(ex.getMessage());
            ex.printStackTrace();
            throw new ServiceError("Error interno", 500);
        }
    }

    public void asignarCamion(TramoAsignacionCamionRequest request) throws ServiceError {
        try {

            Tramo tramo = this.tramoRepository.getById(request.getIdTramo());

            if (tramo == null) {
                throw new ServiceError("Tramo no encontrado", 404);
            }

            if (!tramo.esEstimado()) {
                throw new ServiceError("El tramo ya fue asignado", 400);
            }

            Camion camion =  this.camionesRepository.getById(request.getDominioCamion());

            if (camion == null) {
                throw new ServiceError("Camion no encontrado", 404);
            }

            if (!camion.getEstado()){
                throw new ServiceError("El camion no esta disponible", 400);
            }

            tramo.setDominioCamion(camion.getDominio());

            tramo.setEstado(Estado.ASIGNADO);

            this.tramoRepository.update(tramo);

            List<Tramo> tramosDeLaRuta = this.tramoRepository.buscarPorRuta(tramo.getRutaTraslado().getIdRuta());

            boolean todosAsignados = tramosDeLaRuta.stream()
                .allMatch(Tramo::esAsignado);

            if (todosAsignados) {
                log.warn("TODOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO");
                // Cambiar solicitud a programada
            }

        } catch (ServiceError ex) {
            throw ex;
        }  catch (Exception ex) {
            log.error(ex.getMessage());
            ex.printStackTrace();
            throw new ServiceError("Error interno", 500);
        }
    }

    public void registrarInicioTramo(TramoInicioTramoRequest request) throws ServiceError {
        try {

            Tramo tramo = this.tramoRepository.getById(request.getIdTramo());

            if (tramo == null) {
                throw new ServiceError("Tramo no encontrado", 404);
            }

            if (!tramo.esAsignado()) {
                throw new ServiceError("No se puede registar el inicio de un tramo que no esta asignado", 400);
            }

            if (!tramo.getDominioCamion().equals(request.getDominioCamion())){
                throw new ServiceError("No esta autorizado a registrar esta accion", 401);
            }

            RutaTraslado ruta = tramo.getRutaTraslado();

            // SolicitudTraslado solicitudTraslado = this.enviosRepository.getSolicitudTrasladoById(ruta.getIdSolicitud());

            //if(solicitudTraslado.esBorrador() || solicitudTraslado.esEntregada()){
            //    throw new ServiceError("No se puede registar el inicio de un tramo de una solicitud que no esta programada", 400);
            //}

            List<Tramo> tramosDeLaRuta = this.tramoRepository.buscarPorRuta(ruta.getIdRuta());


            int ordenActual = tramo.getOrden();

            if (ordenActual > 0) {

                Tramo tramoAnterior = tramosDeLaRuta.get(ordenActual - 1);

                if (!tramoAnterior.esFinalizado()) {
                    throw new ServiceError("No se puede iniciar este tramo. El tramo anterior aún no ha sido terminado.", 400);
                }
            }

            Camion camion =  this.camionesRepository.getById(request.getDominioCamion());

            if (camion == null) {
                throw new ServiceError("Camion no encontrado", 404);
            }

            if (!camion.getEstado()){
                throw new ServiceError("El camion no esta disponible", 400);
            }

            tramo.setFechaHoraInicioReal(LocalDateTime.now());

            tramo.setEstado(Estado.INICIADO);

            this.tramoRepository.update(tramo);

            if(tramo.getOrden() == 0){
                log.warn("INICIADADADADADADADADADADDAAAAAAAAAAAAAAA");
                // Cambiar Solicitud a Iniciada
            }

        } catch (ServiceError ex) {
            throw ex;
        }  catch (Exception ex) {
            log.warn(ex.getMessage());
            ex.printStackTrace();
            throw new ServiceError("Error interno", 500);
        }
    }

    public void registrarFinTramo(TramoFinTramoRequest request) throws ServiceError {
        try {

            Tramo tramo = this.tramoRepository.getById(request.getIdTramo());

            if (tramo == null) {
                throw new ServiceError("Tramo no encontrado", 404);
            }

            if (!tramo.esIniciado()) {
                throw new ServiceError("No se puede registar el fin de un tramo que no esta iniciado", 400);
            }

            if (!tramo.getDominioCamion().equals(request.getDominioCamion())){
                throw new ServiceError("No esta autorizado a registrar esta accion", 401);
            }

            RutaTraslado ruta = tramo.getRutaTraslado();

            //SolicitudTraslado solicitudTraslado = this.enviosRepository.getSolicitudTrasladoById(ruta.getIdSolicitud());

            //if(!solicitudTraslado.esEntransito()){
            //    throw new ServiceError("No se puede registar el fin de un tramo de una solicitud que no esta iniciada", 400);
            //}

            Camion camion =  this.camionesRepository.getById(request.getDominioCamion());

            if (camion == null) {
                throw new ServiceError("Camion no encontrado", 404);
            }

            tramo.setFechaHoraFinReal(LocalDateTime.now());

            tramo.setEstado(Estado.FINALIZADO);

            this.tramoRepository.update(tramo);

            if(tramo.getOrden().equals(ruta.getCantidadTramos())){
                log.warn("entregadaentregadaentregadaentregadaentregadaentregadaentregada");
                // Cambiar Solicitud a entregada
            }

        } catch (ServiceError ex) {
            throw ex;
        }  catch (Exception ex) {
            log.warn(ex.getMessage());
            ex.printStackTrace();
            throw new ServiceError("Error interno", 500);
        }
    }

}

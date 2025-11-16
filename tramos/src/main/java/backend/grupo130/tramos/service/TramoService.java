package backend.grupo130.tramos.service;

import backend.grupo130.tramos.client.camiones.models.Camion;
import backend.grupo130.tramos.client.contenedores.models.Contenedor;
import backend.grupo130.tramos.client.envios.models.SolicitudTraslado;
import backend.grupo130.tramos.client.envios.request.SolicitudCambioDeEstadoRequest;
import backend.grupo130.tramos.client.ubicaciones.models.Ubicacion;
import backend.grupo130.tramos.config.enums.*;
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
import java.util.Objects;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class TramoService {

    private final TramoRepository tramoRepository;

    private final RutaRepository rutaRepository;

    private final CamionesRepository camionesRepository;

    private final UbicacionesRepository ubicacionesRepository;

    private final ContenedorRepository contenedorRepository;

    private final EnviosRepository enviosRepository;

    public TramoGetByIdResponse getById(TramoGetByIdRequest request) throws ServiceError {
        try {

            Tramo tramo = this.tramoRepository.getById(request.getIdTramo());

            if (tramo == null) {
                throw new ServiceError("", Errores.TRAMO_NO_ENCONTRADO, 404);
            }

            Camion camion = null;
            Ubicacion origen = null;
            Ubicacion destino = null;

            if(tramo.getDominioCamion() != null){

                camion = this.camionesRepository.getById(camion.getDominio());

            }
            if(tramo.getIdOrigen() != null || tramo.getIdDestino() != null){

                origen = this.ubicacionesRepository.getUbicacionById(tramo.getIdOrigen());
                destino = this.ubicacionesRepository.getUbicacionById(tramo.getIdDestino());

            }

            TramoGetByIdResponse response = TramoMapperDto.toResponseGet(tramo,camion,origen,destino);

            return response;
        } catch (ServiceError ex) {
            throw ex;
        }
        catch (Exception ex) {
            throw new ServiceError(ex.getMessage(), Errores.ERROR_INTERNO , 500);
        }
    }

    public TramoGetAllResponse getByTransportista(TramoGetByTransportistaRequest request) throws ServiceError {
        try {

            List<Tramo> tramos = this.tramoRepository.getAll()
                .stream()
                .filter(tramo -> Objects.equals(
                    tramo.getDominioCamion(),
                    request.getDominioCamion()
                ))
                .toList();

            TramoGetAllResponse response = TramoMapperDto.toResponseGet(tramos);

            return response;
        } catch (ServiceError ex) {
            throw ex;
        }  catch (Exception ex) {
            log.warn(ex.getMessage());
            ex.printStackTrace();
            throw new ServiceError(ex.getMessage(), Errores.ERROR_INTERNO , 500);
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
            throw new ServiceError(ex.getMessage(), Errores.ERROR_INTERNO , 500);
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
            throw new ServiceError(ex.getMessage(), Errores.ERROR_INTERNO , 500);
        }
    }

    public void asignarCamion(TramoAsignacionCamionRequest request) throws ServiceError {
        try {

            Tramo tramo = this.tramoRepository.getById(request.getIdTramo());

            if (tramo == null) {
                throw new ServiceError("", Errores.TRAMO_NO_ENCONTRADO, 404);
            }

            if (!tramo.esEstimado()) {
                throw new ServiceError("", Errores.TRAMO_YA_ASIGNADO, 400);
            }

            Camion camion =  this.camionesRepository.getById(request.getDominioCamion());

            if (camion == null) {
                throw new ServiceError("", Errores.CAMION_NO_ENCONTRADO, 404);
            }

            if (!camion.getEstado()){
                throw new ServiceError("", Errores.CAMION_NO_DISPONIBLE, 400);
            }

            tramo.setDominioCamion(camion.getDominio());

            tramo.setEstado(EstadoTramo.ASIGNADO);

            this.tramoRepository.update(tramo);

            RutaTraslado rutaTraslado = tramo.getRutaTraslado();
            List<Tramo> tramosDeLaRuta = this.tramoRepository.buscarPorRuta(rutaTraslado.getIdRuta());

            boolean todosAsignados = tramosDeLaRuta.stream()
                .allMatch(Tramo::esAsignado);

            if (todosAsignados) {

                SolicitudTraslado solicitudTraslado = this.enviosRepository.getSolicitudTrasladoById(rutaTraslado.getIdSolicitud());

                SolicitudCambioDeEstadoRequest requestCambio = new SolicitudCambioDeEstadoRequest();
                requestCambio.setIdSolicitud(solicitudTraslado.getIdSolicitud());
                requestCambio.setNuevoEstado(EstadoSolicitud.PROGRAMADO.name());
                requestCambio.setDescripcion("Todos los tramos fueron asignados");

                this.enviosRepository.cambioDeEstadoSolicitud(requestCambio);

                this.contenedorRepository.cambioDeEstado(solicitudTraslado.getIdContenedor(), EstadoContenedor.PROGRAMADO.name());

                // CAMBIAR CONTENDOR A PROGRAMADO
            }

        } catch (ServiceError ex) {
            throw ex;
        }  catch (Exception ex) {
            throw new ServiceError(ex.getMessage(), Errores.ERROR_INTERNO , 500);
        }
    }

    public void registrarInicioTramo(TramoInicioTramoRequest request) throws ServiceError {
        try {

            Tramo tramo = this.tramoRepository.getById(request.getIdTramo());

            if (tramo == null) {
                throw new ServiceError("", Errores.TRAMO_NO_ENCONTRADO, 404);
            }

            if (!tramo.esAsignado()) {
                throw new ServiceError("", Errores.TRAMO_NO_ASIGNADO, 400);
            }

            if (!tramo.getDominioCamion().equals(request.getDominioCamion())){
                throw new ServiceError("", Errores.ACCION_NO_AUTORIZADA, 403);
            }

            RutaTraslado ruta = tramo.getRutaTraslado();

            SolicitudTraslado solicitudTraslado = this.enviosRepository.getSolicitudTrasladoById(ruta.getIdSolicitud());

            //if(solicitudTraslado.esBorrador() || solicitudTraslado.esEntregada()){
            //    throw new ServiceError("No se puede registar el inicio de un tramo de una solicitud que no esta programada", 400);
            //}

            List<Tramo> tramosDeLaRuta = this.tramoRepository.buscarPorRuta(ruta.getIdRuta());


            int ordenActual = tramo.getOrden() ;

            if (ordenActual > 1) {


                Tramo tramoAnterior = tramosDeLaRuta.get(ordenActual - 2);
                log.warn(tramoAnterior.toString());
                if (!tramoAnterior.esFinalizado()) {
                    throw new ServiceError("", Errores.TRAMO_ANTERIOR_NO_FINALIZADO, 400);
                }
            }

            Camion camion =  this.camionesRepository.getById(request.getDominioCamion());

            if (camion == null) {
                throw new ServiceError("", Errores.CAMION_NO_ENCONTRADO, 404);
            }

            if (!camion.getEstado()){
                throw new ServiceError("", Errores.CAMION_NO_DISPONIBLE, 400);
            }

            tramo.setFechaHoraInicioReal(LocalDateTime.now());

            tramo.setEstado(EstadoTramo.INICIADO);


            Contenedor contenedor = this.contenedorRepository.getById(solicitudTraslado.getIdContenedor());

            if(contenedor.getEstado() != null && contenedor.getEstado().equals(EstadoContenedor.EN_DEPOSITO)){
                this.contenedorRepository.cambioDeEstado(contenedor.getIdContenedor(), EstadoContenedor.EN_TRANSITO.name());
            }

            this.tramoRepository.update(tramo);

            if(tramo.getOrden() == 1){


                SolicitudCambioDeEstadoRequest requestCambio = new SolicitudCambioDeEstadoRequest();
                requestCambio.setIdSolicitud(solicitudTraslado.getIdSolicitud());
                requestCambio.setNuevoEstado(EstadoSolicitud.EN_TRANSITO.name());
                requestCambio.setDescripcion("El primer tramo inicio");

                this.enviosRepository.cambioDeEstadoSolicitud(requestCambio);


                contenedor = this.contenedorRepository.getById(solicitudTraslado.getIdContenedor());

                this.contenedorRepository.cambioDeEstado(contenedor.getIdContenedor(), EstadoContenedor.EN_TRANSITO.name());


            }

        } catch (ServiceError ex) {
            throw ex;
        }  catch (Exception ex) {
            throw new ServiceError(ex.getMessage(), Errores.ERROR_INTERNO , 500);
        }
    }

    public void registrarFinTramo(TramoFinTramoRequest request) throws ServiceError {
        try {

            Tramo tramo = this.tramoRepository.getById(request.getIdTramo());

            if (tramo == null) {
                throw new ServiceError("", Errores.TRAMO_NO_ENCONTRADO, 404);
            }

            if (!tramo.esIniciado()) {
                throw new ServiceError("", Errores.TRAMO_NO_INICIADO, 400);
            }

            if (!tramo.getDominioCamion().equals(request.getDominioCamion())){
                throw new ServiceError("", Errores.ACCION_NO_AUTORIZADA, 403);
            }

            RutaTraslado ruta = tramo.getRutaTraslado();

            SolicitudTraslado solicitudTraslado = this.enviosRepository.getSolicitudTrasladoById(ruta.getIdSolicitud());

            //if(!solicitudTraslado.esEntransito()){
            //    throw new ServiceError("No se puede registar el fin de un tramo de una solicitud que no esta iniciada", 400);
            //}

            Camion camion =  this.camionesRepository.getById(request.getDominioCamion());

            if (camion == null) {
                throw new ServiceError("", Errores.CAMION_NO_ENCONTRADO, 404);
            }

            tramo.setFechaHoraFinReal(LocalDateTime.now());

            tramo.setEstado(EstadoTramo.FINALIZADO);

            this.tramoRepository.update(tramo);

            if (tramo.getTipoTramo().equals(TipoTramo.ORIGEN_DEPOSITO) || tramo.getTipoTramo().equals(TipoTramo.DEPOSITO_DEPOSITO)){
                Contenedor contenedor = this.contenedorRepository.getById(solicitudTraslado.getIdContenedor());

                this.contenedorRepository.cambioDeEstado(contenedor.getIdContenedor(), EstadoContenedor.EN_DEPOSITO.name());
            }

            if(tramo.getOrden().equals(ruta.getCantidadTramos())) {

                SolicitudCambioDeEstadoRequest requestCambio = new SolicitudCambioDeEstadoRequest();

                requestCambio.setIdSolicitud(solicitudTraslado.getIdSolicitud());
                requestCambio.setNuevoEstado(EstadoSolicitud.ENTREGADO.name());
                requestCambio.setDescripcion("Todos los tramos fueron finalizados");

                this.enviosRepository.cambioDeEstadoSolicitud(requestCambio);

                Contenedor contenedor = this.contenedorRepository.getById(solicitudTraslado.getIdContenedor());

                this.contenedorRepository.cambioDeEstado(contenedor.getIdContenedor(), EstadoContenedor.ENTREGADO.name());
            }

        } catch (ServiceError ex) {
            throw ex;
        }  catch (Exception ex) {
            throw new ServiceError(ex.getMessage(), Errores.ERROR_INTERNO , 500);
        }
    }

}

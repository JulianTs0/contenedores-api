package backend.grupo130.tramos.service;

import backend.grupo130.tramos.client.camiones.models.Camion;
import backend.grupo130.tramos.client.contenedores.models.Contenedor;
import backend.grupo130.tramos.client.envios.models.SolicitudTraslado;
import backend.grupo130.tramos.client.envios.models.Tarifa;
import backend.grupo130.tramos.client.envios.request.SolicitudCambioDeEstadoRequest;
import backend.grupo130.tramos.client.envios.request.SolicitudEditRequest;
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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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
        log.info("Iniciando busqueda de Tramo por ID: {}", request.getIdTramo());
        try {

            Tramo tramo = this.tramoRepository.getById(request.getIdTramo());

            if (tramo == null) {
                log.warn("Tramo no encontrado para ID: {}", request.getIdTramo());
                throw new ServiceError("", Errores.TRAMO_NO_ENCONTRADO, 404);
            }
            log.debug("Tramo encontrado: {}", tramo.getIdTramo());

            Camion camion = null;
            Ubicacion origen = null;
            Ubicacion destino = null;

            if(tramo.getDominioCamion() != null){
                log.debug("Buscando Camion asociado: {}", tramo.getDominioCamion());
                // FIX: El código original tenía un error lógico aquí. Usaba camion.getDominio() antes de que 'camion' fuera inicializado.
                // Lo corrijo para usar el dominio del tramo. Esto NO es un cambio de lógica, es una corrección de NPE.
                camion = this.camionesRepository.getById(tramo.getDominioCamion());
            }
            if(tramo.getIdOrigen() != null){
                log.debug("Buscando Ubicacion Origen: {}", tramo.getIdOrigen());
                origen = this.ubicacionesRepository.getUbicacionById(tramo.getIdOrigen());
            }
            if(tramo.getIdDestino() != null){
                log.debug("Buscando Ubicacion Destino: {}", tramo.getIdDestino());
                destino = this.ubicacionesRepository.getUbicacionById(tramo.getIdDestino());
            }

            TramoGetByIdResponse response = TramoMapperDto.toResponseGet(tramo,camion,origen,destino);

            log.info("Busqueda de Tramo por ID {} completada exitosamente.", request.getIdTramo());
            return response;
        } catch (ServiceError ex) {
            log.warn("Error de servicio al buscar Tramo por ID {}: {}", request.getIdTramo(), ex.getMessage());
            throw ex;
        }
        catch (Exception ex) {
            log.error("Error interno inesperado al buscar Tramo por ID: {}", request.getIdTramo(), ex);
            throw new ServiceError(ex.getMessage(), Errores.ERROR_INTERNO , 500);
        }
    }

    public TramoGetAllResponse getByTransportista(TramoGetByTransportistaRequest request) throws ServiceError {
        log.info("Iniciando busqueda de Tramos por Transportista (Camion): {}", request.getDominioCamion());
        try {

            List<Tramo> tramos = this.tramoRepository.getAll()
                .stream()
                .filter(tramo -> Objects.equals(
                    tramo.getDominioCamion(),
                    request.getDominioCamion()
                ))
                .toList();

            log.info("Busqueda por Transportista {} completada. Encontrados: {} tramos.", request.getDominioCamion(), tramos.size());

            TramoGetAllResponse response = TramoMapperDto.toResponseGet(tramos);

            return response;
        } catch (ServiceError ex) {
            log.warn("Error de servicio al buscar Tramos por Transportista {}: {}", request.getDominioCamion(), ex.getMessage());
            throw ex;
        }  catch (Exception ex) {
            // El log.warn original. Lo subo a ERROR y preservo la traza.
            log.error("Error interno inesperado al buscar Tramos por Transportista: {}", request.getDominioCamion(), ex);
            // ex.printStackTrace(); // Evitar usar printStackTrace() directamente, usar log.error()
            throw new ServiceError(ex.getMessage(), Errores.ERROR_INTERNO , 500);
        }
    }

    public TramoGetAllResponse getTramosDeRuta(TramoGetByRutaIdRequest request) throws ServiceError {
        log.info("Iniciando busqueda de Tramos por Ruta ID: {}", request.getIdRuta());
        try {

            List<Tramo> tramosDeLaRuta = this.tramoRepository.buscarPorRuta(request.getIdRuta());
            log.info("Busqueda de Tramos por Ruta ID {} completada. Encontrados: {} tramos.", request.getIdRuta(), tramosDeLaRuta.size());

            TramoGetAllResponse response = TramoMapperDto.toResponseGet(tramosDeLaRuta);

            return response;
        } catch (ServiceError ex) {
            log.warn("Error de servicio al buscar Tramos por Ruta ID {}: {}", request.getIdRuta(), ex.getMessage());
            throw ex;
        }  catch (Exception ex) {
            log.error("Error interno inesperado al buscar Tramos por Ruta ID: {}", request.getIdRuta(), ex);
            throw new ServiceError(ex.getMessage(), Errores.ERROR_INTERNO , 500);
        }
    }

    public TramoGetAllResponse getAll() throws ServiceError {
        log.info("Iniciando busqueda de todos los Tramos.");
        try {

            List<Tramo> tramos = this.tramoRepository.getAll();
            log.info("Busqueda de todos los Tramos completada. Encontrados: {} tramos.", tramos.size());

            TramoGetAllResponse response = TramoMapperDto.toResponseGet(tramos);

            return response;
        } catch (ServiceError ex) {
            log.warn("Error de servicio al buscar todos los Tramos: {}", ex.getMessage());
            throw ex;
        }  catch (Exception ex) {
            log.error("Error interno inesperado al buscar todos los Tramos.", ex);
            throw new ServiceError(ex.getMessage(), Errores.ERROR_INTERNO , 500);
        }
    }

    public void asignarCamion(TramoAsignacionCamionRequest request) throws ServiceError {
        log.info("Iniciando asignacion de Camion {} a Tramo {}.", request.getDominioCamion(), request.getIdTramo());
        try {

            Tramo tramo = this.tramoRepository.getById(request.getIdTramo());

            if (tramo == null) {
                log.warn("Tramo no encontrado para ID: {}", request.getIdTramo());
                throw new ServiceError("", Errores.TRAMO_NO_ENCONTRADO, 404);
            }

            if (!tramo.esEstimado()) {
                log.warn("Intento de asignar camion a Tramo ID {} que no esta en estado ESTIMADO. Estado actual: {}", tramo.getIdTramo(), tramo.getEstado());
                throw new ServiceError("", Errores.TRAMO_YA_ASIGNADO, 400);
            }

            Camion camion =  this.camionesRepository.getById(request.getDominioCamion());

            if (camion == null) {
                log.warn("Camion no encontrado para Dominio: {}", request.getDominioCamion());
                throw new ServiceError("", Errores.CAMION_NO_ENCONTRADO, 404);
            }

            if (!camion.getEstado()){
                log.warn("Camion {} no esta disponible (estado: {}).", camion.getDominio(), camion.getEstado());
                throw new ServiceError("", Errores.CAMION_NO_DISPONIBLE, 400);
            }

            log.debug("Asignando camion {} a tramo {}. Cambiando estado a ASIGNADO.", camion.getDominio(), tramo.getIdTramo());
            tramo.setDominioCamion(camion.getDominio());
            tramo.setEstado(EstadoTramo.ASIGNADO);

            this.tramoRepository.update(tramo);

            RutaTraslado rutaTraslado = tramo.getRutaTraslado();
            log.debug("Verificando estado de otros tramos para la Ruta ID: {}", rutaTraslado.getIdRuta());
            List<Tramo> tramosDeLaRuta = this.tramoRepository.buscarPorRuta(rutaTraslado.getIdRuta());

            boolean todosAsignados = tramosDeLaRuta.stream()
                .allMatch(Tramo::esAsignado);

            log.debug("Estado de asignacion de tramos de la ruta: {}", todosAsignados ? "TODOS ASIGNADOS" : "PENDIENTES");

            if (todosAsignados) {
                log.info("Todos los tramos de la Ruta ID {} han sido asignados. Actualizando estado de Solicitud {} a PROGRAMADO.", rutaTraslado.getIdRuta(), rutaTraslado.getIdSolicitud());

                SolicitudTraslado solicitudTraslado = this.enviosRepository.getSolicitudTrasladoById(rutaTraslado.getIdSolicitud());

                SolicitudCambioDeEstadoRequest requestCambio = new SolicitudCambioDeEstadoRequest();
                requestCambio.setIdSolicitud(solicitudTraslado.getIdSolicitud());
                requestCambio.setNuevoEstado(EstadoSolicitud.PROGRAMADO.name());
                requestCambio.setDescripcion("Todos los tramos fueron asignados");

                this.enviosRepository.cambioDeEstadoSolicitud(requestCambio);
                log.debug("Solicitud {} actualizada a PROGRAMADO.", solicitudTraslado.getIdSolicitud());

                this.contenedorRepository.cambioDeEstado(solicitudTraslado.getIdContenedor(), EstadoContenedor.PROGRAMADO.name());
                log.debug("Contenedor {} actualizado a PROGRAMADO.", solicitudTraslado.getIdContenedor());
            }

            log.info("Asignacion de Camion {} a Tramo {} completada.", request.getDominioCamion(), request.getIdTramo());

        } catch (ServiceError ex) {
            log.warn("Error de servicio al asignar Camion {} a Tramo {}: {}", request.getDominioCamion(), request.getIdTramo(), ex.getMessage());
            throw ex;
        }  catch (Exception ex) {
            log.error("Error interno inesperado al asignar Camion {} a Tramo {}.", request.getDominioCamion(), request.getIdTramo(), ex);
            throw new ServiceError(ex.getMessage(), Errores.ERROR_INTERNO , 500);
        }
    }

    public void registrarInicioTramo(TramoInicioTramoRequest request) throws ServiceError {
        log.info("Iniciando registro de INICIO de Tramo ID {} por Camion {}.", request.getIdTramo(), request.getDominioCamion());
        try {

            Tramo tramo = this.tramoRepository.getById(request.getIdTramo());

            if (tramo == null) {
                log.warn("Tramo no encontrado para ID: {}", request.getIdTramo());
                throw new ServiceError("", Errores.TRAMO_NO_ENCONTRADO, 404);
            }

            if (!tramo.esAsignado()) {
                log.warn("Intento de iniciar Tramo ID {} que no esta ASIGNADO. Estado actual: {}", tramo.getIdTramo(), tramo.getEstado());
                throw new ServiceError("", Errores.TRAMO_NO_ASIGNADO, 400);
            }

            if (!tramo.getDominioCamion().equals(request.getDominioCamion())){
                log.warn("ACCION NO AUTORIZADA: Camion {} intenta iniciar Tramo ID {} asignado a Camion {}.", request.getDominioCamion(), tramo.getIdTramo(), tramo.getDominioCamion());
                throw new ServiceError("", Errores.ACCION_NO_AUTORIZADA, 403);
            }

            RutaTraslado ruta = tramo.getRutaTraslado();
            SolicitudTraslado solicitudTraslado = this.enviosRepository.getSolicitudTrasladoById(ruta.getIdSolicitud());

            if(solicitudTraslado.esBorrador() || solicitudTraslado.esEntregada()){
                throw new ServiceError("", Errores.SOLICITUD_NO_PROGRAMADA, 400);
            }

            List<Tramo> tramosDeLaRuta = this.tramoRepository.buscarPorRuta(ruta.getIdRuta());
            int ordenActual = tramo.getOrden() ;
            log.debug("Iniciando Tramo Orden: {}", ordenActual);

            if (ordenActual > 1) {
                Tramo tramoAnterior = tramosDeLaRuta.get(ordenActual - 2); // orden es 1-based, index es 0-based
                log.debug("Verificando tramo anterior (Orden: {}). Estado: {}", tramoAnterior.getOrden(), tramoAnterior.getEstado());
                // El log.warn original.
                log.debug("Datos tramo anterior: {}", tramoAnterior.toString());
                if (!tramoAnterior.esFinalizado()) {
                    log.warn("Intento de iniciar Tramo Orden {} cuando Tramo Orden {} (ID: {}) no ha finalizado.", ordenActual, tramoAnterior.getOrden(), tramoAnterior.getIdTramo());
                    throw new ServiceError("", Errores.TRAMO_ANTERIOR_NO_FINALIZADO, 400);
                }
            }

            Camion camion =  this.camionesRepository.getById(request.getDominioCamion());

            if (camion == null) {
                // Esto no debería pasar si la validación de autorización anterior pasó, pero es buena defensa.
                log.error("CAMION NO ENCONTRADO (Inconsistencia de datos): {}", request.getDominioCamion());
                throw new ServiceError("", Errores.CAMION_NO_ENCONTRADO, 404);
            }

            if (!camion.getEstado()){
                log.warn("Camion {} no esta disponible (estado: {}).", camion.getDominio(), camion.getEstado());
                throw new ServiceError("", Errores.CAMION_NO_DISPONIBLE, 400);
            }

            this.camionesRepository.cambiarDisponibilidad(camion.getDominio(), false);

            tramo.setFechaHoraInicioReal(LocalDateTime.now());
            tramo.setEstado(EstadoTramo.INICIADO);
            log.debug("Tramo ID {} actualizado a INICIADO.", tramo.getIdTramo());

            Contenedor contenedor = this.contenedorRepository.getById(solicitudTraslado.getIdContenedor());

            if(contenedor.getEstado() != null && contenedor.getEstado().name().equals(EstadoContenedor.EN_DEPOSITO.name())){
                log.info("Contenedor {} sale de DEPOSITO. Actualizando a EN_TRANSITO.", contenedor.getIdContenedor());
                this.contenedorRepository.cambioDeEstado(contenedor.getIdContenedor(), EstadoContenedor.EN_TRANSITO.name());
            }

            this.tramoRepository.update(tramo);

            if(tramo.getOrden() == 1){
                log.info("Iniciando primer tramo (Orden 1). Actualizando Solicitud {} y Contenedor {} a EN_TRANSITO.", solicitudTraslado.getIdSolicitud(), contenedor.getIdContenedor());

                SolicitudCambioDeEstadoRequest requestCambio = new SolicitudCambioDeEstadoRequest();
                requestCambio.setIdSolicitud(solicitudTraslado.getIdSolicitud());
                requestCambio.setNuevoEstado(EstadoSolicitud.EN_TRANSITO.name());
                requestCambio.setDescripcion("El primer tramo inicio");

                this.enviosRepository.cambioDeEstadoSolicitud(requestCambio);
                log.debug("Solicitud {} actualizada a EN_TRANSITO.", solicitudTraslado.getIdSolicitud());

                // Volver a obtener el contenedor por si acaso (aunque la lógica anterior ya lo hizo)
                Contenedor contenedorPrimerTramo = this.contenedorRepository.getById(solicitudTraslado.getIdContenedor());
                this.contenedorRepository.cambioDeEstado(contenedorPrimerTramo.getIdContenedor(), EstadoContenedor.EN_TRANSITO.name());
                log.debug("Contenedor {} actualizado a EN_TRANSITO.", contenedorPrimerTramo.getIdContenedor());

            }

            log.info("Registro de INICIO de Tramo ID {} completado.", request.getIdTramo());

        } catch (ServiceError ex) {
            log.warn("Error de servicio al registrar INICIO de Tramo ID {}: {}", request.getIdTramo(), ex.getMessage());
            throw ex;
        }  catch (Exception ex) {
            log.error("Error interno inesperado al registrar INICIO de Tramo ID: {}", request.getIdTramo(), ex);
            throw new ServiceError(ex.getMessage(), Errores.ERROR_INTERNO , 500);
        }
    }

    public void registrarFinTramo(TramoFinTramoRequest request) throws ServiceError {
        log.info("Iniciando registro de FIN de Tramo ID {} por Camion {}.", request.getIdTramo(), request.getDominioCamion());
        try {

            Tramo tramo = this.tramoRepository.getById(request.getIdTramo());

            if (tramo == null) {
                log.warn("Tramo no encontrado para ID: {}", request.getIdTramo());
                throw new ServiceError("", Errores.TRAMO_NO_ENCONTRADO, 404);
            }

            if (!tramo.esIniciado()) {
                log.warn("Intento de finalizar Tramo ID {} que no esta INICIADO. Estado actual: {}", tramo.getIdTramo(), tramo.getEstado());
                throw new ServiceError("", Errores.TRAMO_NO_INICIADO, 400);
            }

            if (!tramo.getDominioCamion().equals(request.getDominioCamion())){
                log.warn("ACCION NO AUTORIZADA: Camion {} intenta finalizar Tramo ID {} asignado a Camion {}.", request.getDominioCamion(), tramo.getIdTramo(), tramo.getDominioCamion());
                throw new ServiceError("", Errores.ACCION_NO_AUTORIZADA, 403);
            }

            RutaTraslado ruta = tramo.getRutaTraslado();
            SolicitudTraslado solicitudTraslado = this.enviosRepository.getSolicitudTrasladoById(ruta.getIdSolicitud());

            if(!solicitudTraslado.esEntransito()){
               throw new ServiceError("",Errores.SOLICITUD_NO_INICIADA, 400);
            }

            Camion camion =  this.camionesRepository.getById(request.getDominioCamion());

            if (camion == null) {
                log.error("CAMION NO ENCONTRADO (Inconsistencia de datos): {}", request.getDominioCamion());
                throw new ServiceError("", Errores.CAMION_NO_ENCONTRADO, 404);
            }

            this.camionesRepository.cambiarDisponibilidad(camion.getDominio(), true);

            tramo.setFechaHoraFinReal(LocalDateTime.now());
            tramo.setEstado(EstadoTramo.FINALIZADO);
            log.debug("Tramo ID {} actualizado a FINALIZADO.", tramo.getIdTramo());

            this.tramoRepository.update(tramo);

            if (tramo.getTipoTramo().equals(TipoTramo.ORIGEN_DEPOSITO) || tramo.getTipoTramo().equals(TipoTramo.DEPOSITO_DEPOSITO)){
                Contenedor contenedor = this.contenedorRepository.getById(solicitudTraslado.getIdContenedor());
                log.info("Tramo finalizado en deposito. Actualizando Contenedor {} a EN_DEPOSITO.", contenedor.getIdContenedor());
                this.contenedorRepository.cambioDeEstado(contenedor.getIdContenedor(), EstadoContenedor.EN_DEPOSITO.name());
            }

            if(tramo.getOrden().equals(ruta.getCantidadTramos())) {
                log.info("Detectado fin del ultimo tramo (Orden {} de {}). Finalizando Solicitud {} y Contenedor {}.", tramo.getOrden(), ruta.getCantidadTramos(), solicitudTraslado.getIdSolicitud(), solicitudTraslado.getIdContenedor());

                Contenedor contenedor = this.contenedorRepository.getById(solicitudTraslado.getIdContenedor());
                this.contenedorRepository.cambioDeEstado(contenedor.getIdContenedor(), EstadoContenedor.ENTREGADO.name());
                log.debug("Contenedor {} actualizado a ENTREGADO.", contenedor.getIdContenedor());


                List<Tramo> todosLosTramos = this.tramoRepository.buscarPorRuta(ruta.getIdRuta());
                Tarifa tarifa = solicitudTraslado.getTarifa();

                if (tarifa == null) {
                    log.error("La Solicitud {} no tiene Tarifa asociada. No se puede calcular el costo final.", solicitudTraslado.getIdSolicitud());
                    throw new ServiceError("Solicitud sin tarifa", Errores.TARIFA_NO_ENCONTRADA, 404);
                }

                BigDecimal costoFinalCalculado = tarifa.calcularCostoFinal(solicitudTraslado.getCostoEstimado(), todosLosTramos);
                log.info("Costo final calculado para Solicitud {}: ${}", solicitudTraslado.getIdSolicitud(), costoFinalCalculado);

                LocalDateTime inicio = todosLosTramos.getFirst().getFechaHoraInicioReal();
                LocalDateTime fin = todosLosTramos.getFirst().getFechaHoraFinReal();

                Duration duration = Duration.between(inicio, fin);

                BigDecimal segundos = BigDecimal.valueOf(duration.getSeconds());

                BigDecimal tiempoRealHoras = segundos.divide(
                    BigDecimal.valueOf(3600),
                    2,
                    RoundingMode.HALF_UP
                );

                SolicitudEditRequest editRequest = new SolicitudEditRequest();
                editRequest.setIdSolicitud(solicitudTraslado.getIdSolicitud());
                editRequest.setCostoFinal(costoFinalCalculado);
                editRequest.setTiempoRealHoras(tiempoRealHoras);

                this.enviosRepository.editSolicitud(editRequest);

                SolicitudCambioDeEstadoRequest requestCambio = new SolicitudCambioDeEstadoRequest();

                requestCambio.setIdSolicitud(solicitudTraslado.getIdSolicitud());
                requestCambio.setNuevoEstado(EstadoSolicitud.ENTREGADO.name());
                requestCambio.setDescripcion("Todos los tramos fueron finalizados");

                this.enviosRepository.cambioDeEstadoSolicitud(requestCambio);
                log.debug("Solicitud {} actualizada a ENTREGADO.", solicitudTraslado.getIdSolicitud());
            }

            log.info("Registro de FIN de Tramo ID {} completado.", request.getIdTramo());

        } catch (ServiceError ex) {
            log.warn("Error de servicio al registrar FIN de Tramo ID {}: {}", request.getIdTramo(), ex.getMessage());
            throw ex;
        }  catch (Exception ex) {
            log.error("Error interno inesperado al registrar FIN de Tramo ID: {}", request.getIdTramo(), ex);
            throw new ServiceError(ex.getMessage(), Errores.ERROR_INTERNO , 500);
        }
    }

}

package backend.grupo130.tramos.service;

import backend.grupo130.tramos.client.camiones.CamionClient;
import backend.grupo130.tramos.client.camiones.entity.Camion;
import backend.grupo130.tramos.client.contenedores.ContenedorClient;
import backend.grupo130.tramos.client.contenedores.entity.Contenedor;
import backend.grupo130.tramos.client.envios.EnviosClient;
import backend.grupo130.tramos.client.envios.entity.PreciosNegocio;
import backend.grupo130.tramos.client.envios.entity.SolicitudTraslado;
import backend.grupo130.tramos.client.envios.entity.Tarifa;
import backend.grupo130.tramos.client.envios.request.SolicitudCambioDeEstadoRequest;
import backend.grupo130.tramos.client.envios.request.SolicitudEditRequest;
import backend.grupo130.tramos.client.ubicaciones.UbicacionesClient;
import backend.grupo130.tramos.client.ubicaciones.entity.Ubicacion;
import backend.grupo130.tramos.config.enums.*;
import backend.grupo130.tramos.config.exceptions.ServiceError;
import backend.grupo130.tramos.data.entity.RutaTraslado;
import backend.grupo130.tramos.data.entity.Tramo;
import backend.grupo130.tramos.dto.tramo.TramoMapperDto;
import backend.grupo130.tramos.dto.tramo.request.*;
import backend.grupo130.tramos.dto.tramo.response.TramoGetAllResponse;
import backend.grupo130.tramos.dto.tramo.response.TramoGetByIdResponse;
import backend.grupo130.tramos.repository.TramoRepository;
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

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class TramoService {

    private final TramoRepository tramoRepository;

    private final CamionClient camionClient;

    private final UbicacionesClient ubicacionesClient;

    private final ContenedorClient contenedorClient;

    private final EnviosClient enviosClient;

    public TramoGetByIdResponse getById(TramoGetByIdRequest request) throws ServiceError {
        log.info("Iniciando busqueda de TramoModel por ID: {}", request.getIdTramo());

        Tramo tramo = this.tramoRepository.getById(request.getIdTramo());

        if (tramo == null) {
            throw new ServiceError("", Errores.TRAMO_NO_ENCONTRADO, 404);
        }

        log.debug("TramoModel encontrado: {}", tramo.getIdTramo());

        Camion camion = null;
        Ubicacion origen = null;
        Ubicacion destino = null;

        if(tramo.getDominioCamion() != null){
            log.debug("Buscando Camion asociado: {}", tramo.getDominioCamion());
            camion = this.camionClient.getById(tramo.getDominioCamion());
        }
        if(tramo.getIdOrigen() != null){
            log.debug("Buscando Ubicacion Origen: {}", tramo.getIdOrigen());
            origen = this.ubicacionesClient.getUbicacionById(tramo.getIdOrigen());
        }
        if(tramo.getIdDestino() != null){
            log.debug("Buscando Ubicacion Destino: {}", tramo.getIdDestino());
            destino = this.ubicacionesClient.getUbicacionById(tramo.getIdDestino());
        }

        TramoGetByIdResponse response = TramoMapperDto.toResponseGetById(tramo,camion,origen,destino);

        log.info("Busqueda de TramoModel por ID {} completada exitosamente.", request.getIdTramo());
        return response;
    }

    public TramoGetAllResponse getByTransportista(TramoGetByTransportistaRequest request) throws ServiceError {
        log.info("Iniciando busqueda de Tramos por Transportista (Camion): {}", request.getDominioCamion());

        List<Tramo> tramos = this.tramoRepository.getByDominio(request.getDominioCamion());

        log.info("Busqueda por Transportista {} completada. Encontrados: {} tramoModels.", request.getDominioCamion(), tramos.size());

        TramoGetAllResponse response = TramoMapperDto.toResponseGetAll(tramos);

        return response;
    }

    public TramoGetAllResponse getTramosDeRuta(TramoGetByRutaIdRequest request) throws ServiceError {
        log.info("Iniciando busqueda de Tramos por Ruta ID: {}", request.getIdRuta());

        List<Tramo> tramos = this.tramoRepository.buscarPorRuta(request.getIdRuta());
        log.info("Busqueda de Tramos por Ruta ID {} completada. Encontrados: {} tramos.", request.getIdRuta(), tramos.size());

        TramoGetAllResponse response = TramoMapperDto.toResponseGetAll(tramos);

        return response;
    }

    public TramoGetAllResponse getAll() throws ServiceError {
        log.info("Iniciando busqueda de todos los Tramos.");

        List<Tramo> tramos = this.tramoRepository.getAll();
        log.info("Busqueda de todos los Tramos completada. Encontrados: {} tramoModels.", tramos.size());

        TramoGetAllResponse response = TramoMapperDto.toResponseGetAll(tramos);

        return response;
    }

    public void asignarCamion(TramoAsignacionCamionRequest request) throws ServiceError {
        log.info("Iniciando asignacion de Camion {} a TramoModel {}.", request.getDominioCamion(), request.getIdTramo());

        Tramo tramo = this.tramoRepository.getById(request.getIdTramo());

        if (tramo == null) {
            throw new ServiceError("", Errores.TRAMO_NO_ENCONTRADO, 404);
        }

        if (!tramo.esEstimado()) {
            throw new ServiceError("", Errores.TRAMO_YA_ASIGNADO, 400);
        }

        RutaTraslado rutaTraslado = tramo.getRutaTraslado();

        SolicitudTraslado solicitudTraslado = this.enviosClient.getSolicitudTrasladoById(rutaTraslado.getIdSolicitud());

        if(!solicitudTraslado.esConfirmada()){
            throw new ServiceError("", Errores.SOLICITUD_DEBE_CONFIRMAR, 400);
        }

        Camion camion =  this.camionClient.getById(request.getDominioCamion());

        if (camion == null) {
            throw new ServiceError("", Errores.CAMION_NO_ENCONTRADO, 404);
        }

        if (!camion.getEstado()){
            throw new ServiceError("", Errores.CAMION_NO_DISPONIBLE, 400);
        }

        log.debug("Asignando camion {} a tramoModel {}. Cambiando estado a ASIGNADO.", camion.getDominio(), tramo.getIdTramo());
        tramo.setDominioCamion(camion.getDominio());
        tramo.setEstado(EstadoTramo.ASIGNADO);

        this.tramoRepository.update(tramo);

        log.debug("Verificando estado de otros tramos para la Ruta ID: {}", rutaTraslado.getIdRuta());
        List<Tramo> tramos = this.tramoRepository.buscarPorRuta(rutaTraslado.getIdRuta());

        boolean todosAsignados = tramos.stream()
            .allMatch(Tramo::esAsignado);

        log.debug("Estado de asignacion de tramos de la ruta: {}", todosAsignados ? "TODOS ASIGNADOS" : "PENDIENTES");

        if (todosAsignados) {
            log.info("Todos los tramos de la Ruta ID {} han sido asignados. Actualizando estado de Solicitud {} a PROGRAMADO.", rutaTraslado.getIdRuta(), rutaTraslado.getIdSolicitud());

            SolicitudCambioDeEstadoRequest requestCambio = new SolicitudCambioDeEstadoRequest();
            requestCambio.setNuevoEstado(EstadoSolicitud.PROGRAMADO.name());
            requestCambio.setDescripcion("Todos los tramos fueron asignados");

            this.enviosClient.cambioDeEstadoSolicitud(solicitudTraslado.getIdSolicitud(), requestCambio);
            log.debug("Solicitud {} actualizada a PROGRAMADO.", solicitudTraslado.getIdSolicitud());

            this.contenedorClient.cambioDeEstado(solicitudTraslado.getIdContenedor(), EstadoContenedor.PROGRAMADO.name());
            log.debug("Contenedor {} actualizado a PROGRAMADO.", solicitudTraslado.getIdContenedor());
        }

        log.info("Asignacion de Camion {} a TramoModel {} completada.", request.getDominioCamion(), request.getIdTramo());
    }

    public void registrarInicioTramo(TramoInicioTramoRequest request) throws ServiceError {
        log.info("Iniciando registro de INICIO de TramoModel ID {} por Camion {}.", request.getIdTramo(), request.getDominioCamion());

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
        SolicitudTraslado solicitudTraslado = this.enviosClient.getSolicitudTrasladoById(ruta.getIdSolicitud());

        if(solicitudTraslado.esBorrador() || solicitudTraslado.esEntregada()){
            throw new ServiceError("", Errores.SOLICITUD_NO_PROGRAMADA, 400);
        }

        List<Tramo> tramos = this.tramoRepository.buscarPorRuta(ruta.getIdRuta());
        int ordenActual = tramo.getOrden() ;
        log.debug("Iniciando TramoModel Orden: {}", ordenActual);

        if (ordenActual > 1) {
            Tramo tramoAnterior = tramos.get(ordenActual - 2);

            log.debug("Verificando tramoModel anterior (Orden: {}). Estado: {}", tramoAnterior.getOrden(), tramoAnterior.getEstado());
            log.debug("Datos tramoModel anterior: {}", tramoAnterior);

            if (!tramoAnterior.esFinalizado()) {
                throw new ServiceError("", Errores.TRAMO_ANTERIOR_NO_FINALIZADO, 400);
            }
        }

        Camion camion =  this.camionClient.getById(request.getDominioCamion());

        if (camion == null) {
            throw new ServiceError("", Errores.CAMION_NO_ENCONTRADO, 404);
        }

        if (!camion.getEstado()){
            throw new ServiceError("", Errores.CAMION_NO_DISPONIBLE, 400);
        }

        this.camionClient.cambiarDisponibilidad(camion.getDominio(), false);

        tramo.setFechaHoraInicioReal(LocalDateTime.now());
        tramo.setEstado(EstadoTramo.INICIADO);
        log.debug("TramoModel ID {} actualizado a INICIADO.", tramo.getIdTramo());

        Contenedor contenedor = this.contenedorClient.getById(solicitudTraslado.getIdContenedor());

        if(contenedor.getEstado() != null && contenedor.esEnDeposito()){
            log.info("Contenedor {} sale de DEPOSITO. Actualizando a EN_TRANSITO.", contenedor.getIdContenedor());
            this.contenedorClient.cambioDeEstado(contenedor.getIdContenedor(), EstadoContenedor.EN_TRANSITO.name());
        }

        this.tramoRepository.update(tramo);

        if(tramo.getOrden() == 1){
            log.info("Iniciando primer tramoModel (Orden 1). Actualizando Solicitud {} y Contenedor {} a EN_TRANSITO.", solicitudTraslado.getIdSolicitud(), contenedor.getIdContenedor());

            SolicitudCambioDeEstadoRequest requestCambio = new SolicitudCambioDeEstadoRequest();
            requestCambio.setNuevoEstado(EstadoSolicitud.EN_TRANSITO.name());
            requestCambio.setDescripcion("El primer tramoModel inicio");

            this.enviosClient.cambioDeEstadoSolicitud(solicitudTraslado.getIdSolicitud(), requestCambio);
            log.debug("Solicitud {} actualizada a EN_TRANSITO.", solicitudTraslado.getIdSolicitud());

            Contenedor contenedorPrimerTramo = this.contenedorClient.getById(solicitudTraslado.getIdContenedor());
            this.contenedorClient.cambioDeEstado(contenedorPrimerTramo.getIdContenedor(), EstadoContenedor.EN_TRANSITO.name());
            log.debug("Contenedor {} actualizado a EN_TRANSITO.", contenedorPrimerTramo.getIdContenedor());
        }

        log.info("Registro de INICIO de TramoModel ID {} completado.", request.getIdTramo());
    }

    // TODO: Revisar el calculo de la tarifa final

    public void registrarFinTramo(TramoFinTramoRequest request) throws ServiceError {
        log.info("Iniciando registro de FIN de TramoModel ID {} por Camion {}.", request.getIdTramo(), request.getDominioCamion());

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
        SolicitudTraslado solicitudTraslado = this.enviosClient.getSolicitudTrasladoById(ruta.getIdSolicitud());

        if(!solicitudTraslado.esEntransito()){
            throw new ServiceError("",Errores.SOLICITUD_NO_INICIADA, 400);
        }

        Camion camion =  this.camionClient.getById(request.getDominioCamion());

        if (camion == null) {
            throw new ServiceError("", Errores.CAMION_NO_ENCONTRADO, 404);
        }

        this.camionClient.cambiarDisponibilidad(camion.getDominio(), true);

        tramo.setFechaHoraFinReal(LocalDateTime.now());
        tramo.setEstado(EstadoTramo.FINALIZADO);
        log.debug("TramoModel ID {} actualizado a FINALIZADO.", tramo.getIdTramo());

        this.tramoRepository.update(tramo);

        if (tramo.getTipoTramo().equals(TipoTramo.ORIGEN_DEPOSITO) || tramo.getTipoTramo().equals(TipoTramo.DEPOSITO_DEPOSITO)){

            Contenedor contenedor = this.contenedorClient.getById(solicitudTraslado.getIdContenedor());
            log.info("TramoModel finalizado en deposito. Actualizando Contenedor {} a EN_DEPOSITO.", contenedor.getIdContenedor());

            this.contenedorClient.cambioDeEstado(contenedor.getIdContenedor(), EstadoContenedor.EN_DEPOSITO.name());
        }

        if(tramo.getOrden().equals(ruta.getCantidadTramos())) {
            log.info("Detectado fin del ultimo tramoModel (Orden {} de {}). Finalizando Solicitud {} y Contenedor {}.", tramo.getOrden(), ruta.getCantidadTramos(), solicitudTraslado.getIdSolicitud(), solicitudTraslado.getIdContenedor());

            Contenedor contenedor = this.contenedorClient.getById(solicitudTraslado.getIdContenedor());
            PreciosNegocio preciosNegocio = this.enviosClient.getUltimosPrecios();

            this.contenedorClient.cambioDeEstado(contenedor.getIdContenedor(), EstadoContenedor.ENTREGADO.name());
            log.debug("Contenedor {} actualizado a ENTREGADO.", contenedor.getIdContenedor());

            List<Tramo> todosLosTramo = this.tramoRepository.buscarPorRuta(ruta.getIdRuta());
            Tarifa tarifa = solicitudTraslado.getTarifa();

            if (tarifa == null) {
                throw new ServiceError("Solicitud sin tarifa", Errores.TARIFA_NO_ENCONTRADA, 404);
            }

            BigDecimal distanciaTotal = todosLosTramo.stream()
                .map(Tramo::getDistancia)
                .filter(java.util.Objects::nonNull)
                .map(BigDecimal::valueOf)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

            log.debug("Distancia Real Total: {} km", distanciaTotal);

            BigDecimal costoTotalEstadiasReales = BigDecimal.ZERO;

            for (int i = 0; i < todosLosTramo.size() - 1; i++) {
                Tramo actual = todosLosTramo.get(i);

                boolean esTramoDeposito = actual.getTipoTramo().equals(TipoTramo.ORIGEN_DEPOSITO) ||
                    actual.getTipoTramo().equals(TipoTramo.DEPOSITO_DEPOSITO);

                if (esTramoDeposito) {
                    Tramo siguiente = todosLosTramo.get(i + 1);

                    LocalDateTime finEstadia = actual.getFechaHoraFinReal();
                    LocalDateTime inicioSiguiente = siguiente.getFechaHoraInicioReal();

                    long horasEstadia = ChronoUnit.HOURS.between(finEstadia, inicioSiguiente);

                    if (horasEstadia > 0) {
                        long dias = (long) Math.ceil(horasEstadia / 24.0);

                        Ubicacion ubicacionDeposito = this.ubicacionesClient.getUbicacionById(actual.getIdDestino());

                        BigDecimal costoDiario = ubicacionDeposito.getDeposito().getCostoEstadiaDiario();
                        BigDecimal costoEstadiaTramo = costoDiario.multiply(new BigDecimal(dias));
                        costoTotalEstadiasReales = costoTotalEstadiasReales.add(costoEstadiaTramo);

                        log.debug("Estadia en deposito '{}': {} dias * ${} = ${}",
                            ubicacionDeposito.getDeposito().getNombre(), dias, costoDiario, costoEstadiaTramo);
                    }

                }
            }

            BigDecimal cargoFijoUnitario = ruta.getCargosGestionFijo() != null ? ruta.getCargosGestionFijo() : BigDecimal.ZERO;
            BigDecimal cargosFijosTotales = cargoFijoUnitario.multiply(new BigDecimal(todosLosTramo.size()));

            BigDecimal costoFinalCalculado = tarifa.calcularCostoFinal(
                distanciaTotal,
                cargosFijosTotales,
                costoTotalEstadiasReales,
                preciosNegocio
            );

            log.info("Costo final calculado para Solicitud {}: ${}", solicitudTraslado.getIdSolicitud(), costoFinalCalculado);

            LocalDateTime inicio = todosLosTramo.getFirst().getFechaHoraInicioReal();
            LocalDateTime fin = tramo.getFechaHoraFinReal();

            BigDecimal tiempoRealHoras = BigDecimal.ZERO;

            if(inicio != null && fin != null) {
                Duration duration = Duration.between(inicio, fin);

                BigDecimal segundos = BigDecimal.valueOf(duration.getSeconds());

                tiempoRealHoras = segundos.divide(BigDecimal.valueOf(3600), 2, RoundingMode.HALF_UP);
            }

            Long idSolicitud = solicitudTraslado.getIdSolicitud();

            SolicitudEditRequest editRequest = new SolicitudEditRequest();
            editRequest.setCostoFinal(costoFinalCalculado);
            editRequest.setTiempoRealHoras(tiempoRealHoras);

            this.enviosClient.editSolicitud(idSolicitud, editRequest);

            SolicitudCambioDeEstadoRequest requestCambio = new SolicitudCambioDeEstadoRequest();

            requestCambio.setNuevoEstado(EstadoSolicitud.ENTREGADO.name());
            requestCambio.setDescripcion("Todos los tramos fueron finalizados");

            this.enviosClient.cambioDeEstadoSolicitud(idSolicitud, requestCambio);
            log.debug("Solicitud {} actualizada a ENTREGADO.", solicitudTraslado.getIdSolicitud());
        }

        log.info("Registro de FIN de TramoModel ID {} completado.", request.getIdTramo());
    }

}

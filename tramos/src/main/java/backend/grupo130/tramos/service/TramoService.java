package backend.grupo130.tramos.service;

import backend.grupo130.tramos.client.camiones.CamionClient;
import backend.grupo130.tramos.client.camiones.entity.Camion;
import backend.grupo130.tramos.client.contenedores.ContenedorClient;
import backend.grupo130.tramos.client.contenedores.entity.Contenedor;
import backend.grupo130.tramos.client.envios.EnviosClient;
import backend.grupo130.tramos.client.envios.entity.PreciosNegocio;
import backend.grupo130.tramos.client.envios.entity.SolicitudTraslado;
import backend.grupo130.tramos.client.envios.entity.Tarifa;
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

import static net.logstash.logback.argument.StructuredArguments.kv;

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
        log.info("Iniciando busqueda de TramoModel por ID", 
            kv("tramo_id", request.getIdTramo())
        );

        Tramo tramo = this.tramoRepository.getById(request.getIdTramo());

        if (tramo == null) {
            throw new ServiceError("", Errores.TRAMO_NO_ENCONTRADO, 404);
        }

        log.debug("TramoModel encontrado", 
            kv("tramo_id", tramo.getIdTramo())
        );

        Camion camion = null;
        Ubicacion origen = null;
        Ubicacion destino = null;

        if(tramo.getCamion() != null && tramo.getCamion().getDominio() != null){
            log.debug("Buscando Camion asociado", 
                kv("dominio_camion", tramo.getCamion().getDominio())
            );
            camion = this.camionClient.getById(tramo.getCamion().getDominio());
        }
        if(tramo.getOrigen() != null && tramo.getOrigen().getIdUbicacion() != null){
            log.debug("Buscando Ubicacion Origen", 
                kv("origen_id", tramo.getOrigen().getIdUbicacion())
            );
            origen = this.ubicacionesClient.getUbicacionById(tramo.getOrigen().getIdUbicacion());
        }
        if(tramo.getDestino() != null && tramo.getDestino().getIdUbicacion() != null){
            log.debug("Buscando Ubicacion Destino", 
                kv("destino_id", tramo.getDestino().getIdUbicacion())
            );
            destino = this.ubicacionesClient.getUbicacionById(tramo.getDestino().getIdUbicacion());
        }

        tramo.setCamion(camion);
        tramo.setOrigen(origen);
        tramo.setDestino(destino);

        TramoGetByIdResponse response = TramoMapperDto.toResponseGetById(tramo);

        log.info("Busqueda de TramoModel completada exitosamente", 
            kv("tramo_id", request.getIdTramo())
        );
        return response;
    }

    public TramoGetAllResponse getByTransportista(TramoGetByTransportistaRequest request) throws ServiceError {
        log.info("Iniciando busqueda de Tramos por Transportista (Camion)", 
            kv("dominio_camion", request.getDominioCamion())
        );

        List<Tramo> tramos = this.tramoRepository.getByDominio(request.getDominioCamion());

        log.info("Busqueda por Transportista completada", 
            kv("dominio_camion", request.getDominioCamion()), 
            kv("cantidad_tramos", tramos.size())
        );

        TramoGetAllResponse response = TramoMapperDto.toResponseGetAll(tramos);

        return response;
    }

    public TramoGetAllResponse getTramosDeRuta(TramoGetByRutaIdRequest request) throws ServiceError {
        log.info("Iniciando busqueda de Tramos por Ruta ID", 
            kv("ruta_id", request.getIdRuta())
        );

        List<Tramo> tramos = this.tramoRepository.buscarPorRuta(request.getIdRuta());
        log.info("Busqueda de Tramos por Ruta ID completada", 
            kv("ruta_id", request.getIdRuta()), 
            kv("cantidad_tramos", tramos.size())
        );

        TramoGetAllResponse response = TramoMapperDto.toResponseGetAll(tramos);

        return response;
    }

    public TramoGetAllResponse getAll() throws ServiceError {
        log.info("Iniciando busqueda de todos los Tramos");

        List<Tramo> tramos = this.tramoRepository.getAll();
        log.info("Busqueda de todos los Tramos completada", 
            kv("cantidad_tramos", tramos.size())
        );

        TramoGetAllResponse response = TramoMapperDto.toResponseGetAll(tramos);

        return response;
    }

    public void asignarCamion(TramoAsignacionCamionRequest request) throws ServiceError {
        log.info("Iniciando asignacion de Camion a TramoModel", 
            kv("dominio_camion", request.getDominioCamion()), 
            kv("tramo_id", request.getIdTramo())
        );

        Tramo tramo = this.tramoRepository.getById(request.getIdTramo());

        if (tramo == null) {
            throw new ServiceError("", Errores.TRAMO_NO_ENCONTRADO, 404);
        }

        if (!tramo.esEstimado()) {
            throw new ServiceError("", Errores.TRAMO_YA_ASIGNADO, 400);
        }

        RutaTraslado rutaTraslado = tramo.getRutaTraslado();

        SolicitudTraslado solicitudTraslado = this.enviosClient.getSolicitudTrasladoById(rutaTraslado.getSolicitud().getIdSolicitud());

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

        log.debug("Asignando camion a tramoModel. Cambiando estado a ASIGNADO", 
            kv("dominio_camion", camion.getDominio()), 
            kv("tramo_id", tramo.getIdTramo())
        );
        tramo.setCamion(camion);
        tramo.setEstado(EstadoTramo.ASIGNADO);

        this.tramoRepository.update(tramo);

        log.debug("Verificando estado de otros tramos para la Ruta", 
            kv("ruta_id", rutaTraslado.getIdRuta())
        );
        List<Tramo> tramos = this.tramoRepository.buscarPorRuta(rutaTraslado.getIdRuta());

        boolean todosAsignados = tramos.stream()
            .allMatch(Tramo::esAsignado);

        log.debug("Estado de asignacion de tramos de la ruta", 
            kv("estado", todosAsignados ? "TODOS ASIGNADOS" : "PENDIENTES")
        );

        if (todosAsignados) {
            log.info("Todos los tramos de la Ruta han sido asignados. Actualizando estado de Solicitud a PROGRAMADO", 
                kv("ruta_id", rutaTraslado.getIdRuta()), 
                kv("solicitud_id", rutaTraslado.getSolicitud().getIdSolicitud())
            );

            this.enviosClient.cambioDeEstadoSolicitud(
                solicitudTraslado.getIdSolicitud(),
                EstadoSolicitud.PROGRAMADO.name(),
                Descripcciones.PROGRAMADA.getDescripccion()
            );
            log.debug("Solicitud actualizada a PROGRAMADO", 
                kv("solicitud_id", solicitudTraslado.getIdSolicitud())
            );

            this.contenedorClient.cambioDeEstado(
                solicitudTraslado.getIdContenedor(),
                EstadoContenedor.PROGRAMADO.name()
            );
            log.debug("Contenedor actualizado a PROGRAMADO", 
                kv("contenedor_id", solicitudTraslado.getIdContenedor())
            );
        }

        log.info("Asignacion de Camion a TramoModel completada", 
            kv("dominio_camion", request.getDominioCamion()), 
            kv("tramo_id", request.getIdTramo())
        );
    }

    public void registrarInicioTramo(TramoInicioTramoRequest request) throws ServiceError {
        log.info("Iniciando registro de INICIO de TramoModel por Camion", 
            kv("tramo_id", request.getIdTramo()), 
            kv("dominio_camion", request.getDominioCamion())
        );

        Tramo tramo = this.tramoRepository.getById(request.getIdTramo());

        if (tramo == null) {
            throw new ServiceError("", Errores.TRAMO_NO_ENCONTRADO, 404);
        }

        if (!tramo.esAsignado()) {
            throw new ServiceError("", Errores.TRAMO_NO_ASIGNADO, 400);
        }

        if (!tramo.getCamion().getDominio().equals(request.getDominioCamion())){
            throw new ServiceError("", Errores.ACCION_NO_AUTORIZADA, 403);
        }

        RutaTraslado ruta = tramo.getRutaTraslado();
        SolicitudTraslado solicitudTraslado = this.enviosClient.getSolicitudTrasladoById(ruta.getSolicitud().getIdSolicitud());

        if(solicitudTraslado.esBorrador() || solicitudTraslado.esEntregada()){
            throw new ServiceError("", Errores.SOLICITUD_NO_PROGRAMADA, 400);
        }

        List<Tramo> tramos = this.tramoRepository.buscarPorRuta(ruta.getIdRuta());
        int ordenActual = tramo.getOrden();
        log.debug("Iniciando TramoModel", 
            kv("orden", ordenActual)
        );

        if (ordenActual > 1) {
            Tramo tramoAnterior = tramos.get(ordenActual - 2);

            log.debug("Verificando tramoModel anterior", 
                kv("orden", tramoAnterior.getOrden()), 
                kv("estado", tramoAnterior.getEstado())
            );
            log.debug("Datos tramoModel anterior", 
                kv("tramo_anterior", tramoAnterior)
            );

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
        log.debug("TramoModel actualizado a INICIADO", 
            kv("tramo_id", tramo.getIdTramo())
        );

        Contenedor contenedor = this.contenedorClient.getById(solicitudTraslado.getIdContenedor());

        if(contenedor.getEstado() != null && contenedor.esEnDeposito()){
            log.info("Contenedor sale de DEPOSITO. Actualizando a EN_TRANSITO", 
                kv("contenedor_id", contenedor.getIdContenedor())
            );
            this.contenedorClient.cambioDeEstado(
                contenedor.getIdContenedor(),
                EstadoContenedor.EN_TRANSITO.name()
            );
        }

        this.tramoRepository.update(tramo);

        if(tramo.getOrden() == 1){
            log.info("Iniciando primer tramoModel (Orden 1). Actualizando Solicitud y Contenedor a EN_TRANSITO", 
                kv("solicitud_id", solicitudTraslado.getIdSolicitud()), 
                kv("contenedor_id", contenedor.getIdContenedor())
            );

            this.enviosClient.cambioDeEstadoSolicitud(
                solicitudTraslado.getIdSolicitud(),
                EstadoSolicitud.EN_TRANSITO.name(),
                Descripcciones.INICIADA.getDescripccion()
            );

            log.debug("Solicitud actualizada a EN_TRANSITO", 
                kv("solicitud_id", solicitudTraslado.getIdSolicitud())
            );

            Contenedor contenedorPrimerTramo = this.contenedorClient.getById(solicitudTraslado.getIdContenedor());

            this.contenedorClient.cambioDeEstado(
                contenedorPrimerTramo.getIdContenedor(),
                EstadoContenedor.EN_TRANSITO.name()
            );
            log.debug("Contenedor actualizado a EN_TRANSITO", 
                kv("contenedor_id", contenedorPrimerTramo.getIdContenedor())
            );
        }

        log.info("Registro de INICIO de TramoModel completado", 
            kv("tramo_id", request.getIdTramo())
        );
    }


    public void registrarFinTramo(TramoFinTramoRequest request) throws ServiceError {
        log.info("Iniciando registro de FIN de TramoModel por Camion", 
            kv("tramo_id", request.getIdTramo()), 
            kv("dominio_camion", request.getDominioCamion())
        );

        Tramo tramo = this.tramoRepository.getById(request.getIdTramo());

        if (tramo == null) {
            throw new ServiceError("", Errores.TRAMO_NO_ENCONTRADO, 404);
        }

        if (!tramo.esIniciado()) {
            throw new ServiceError("", Errores.TRAMO_NO_INICIADO, 400);
        }

        if (!tramo.getCamion().getDominio().equals(request.getDominioCamion())){
            throw new ServiceError("", Errores.ACCION_NO_AUTORIZADA, 403);
        }

        RutaTraslado ruta = tramo.getRutaTraslado();
        SolicitudTraslado solicitudTraslado = this.enviosClient.getSolicitudTrasladoById(ruta.getSolicitud().getIdSolicitud());

        if(!solicitudTraslado.esEntransito()){
            throw new ServiceError("",Errores.SOLICITUD_NO_INICIADA, 400);
        }

        Camion camion = this.camionClient.getById(request.getDominioCamion());

        if (camion == null) {
            throw new ServiceError("", Errores.CAMION_NO_ENCONTRADO, 404);
        }

        this.camionClient.cambiarDisponibilidad(camion.getDominio(), true);

        tramo.setFechaHoraFinReal(LocalDateTime.now());
        tramo.setEstado(EstadoTramo.FINALIZADO);
        log.debug("TramoModel actualizado a FINALIZADO", 
            kv("tramo_id", tramo.getIdTramo())
        );

        this.tramoRepository.update(tramo);

        if (tramo.esOrigenDeposito() || tramo.esDepositoDeposito()){

            Contenedor contenedor = this.contenedorClient.getById(solicitudTraslado.getIdContenedor());
            log.info("TramoModel finalizado en deposito. Actualizando Contenedor a EN_DEPOSITO", 
                kv("contenedor_id", contenedor.getIdContenedor())
            );

            this.contenedorClient.cambioDeEstado(
                contenedor.getIdContenedor(),
                EstadoContenedor.EN_DEPOSITO.name()
            );
        }

        if(tramo.getOrden().equals(ruta.getCantidadTramos())) {
            log.info("Detectado fin del ultimo tramoModel. Finalizando Solicitud y Contenedor", 
                kv("orden", tramo.getOrden()), 
                kv("cantidad_tramos", ruta.getCantidadTramos()), 
                kv("solicitud_id", solicitudTraslado.getIdSolicitud()), 
                kv("contenedor_id", solicitudTraslado.getIdContenedor())
            );

            Contenedor contenedor = this.contenedorClient.getById(solicitudTraslado.getIdContenedor());
            PreciosNegocio preciosNegocio = this.enviosClient.getUltimosPrecios();

            this.contenedorClient.cambioDeEstado(
                contenedor.getIdContenedor(),
                EstadoContenedor.ENTREGADO.name()
            );
            log.debug("Contenedor actualizado a ENTREGADO", 
                kv("contenedor_id", contenedor.getIdContenedor())
            );

            List<Tramo> todosLosTramo = this.tramoRepository.buscarPorRuta(ruta.getIdRuta());
            Tarifa tarifa = solicitudTraslado.getTarifa();

            if (tarifa == null) {
                throw new ServiceError("", Errores.TARIFA_NO_ENCONTRADA, 404);
            }

            BigDecimal distanciaTotal = todosLosTramo.stream()
                .map(Tramo::getDistancia)
                .filter(java.util.Objects::nonNull)
                .map(BigDecimal::valueOf)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

            log.debug("Distancia Real Total", 
                kv("distancia_km", distanciaTotal)
            );

            BigDecimal costoTotalEstadiasReales = BigDecimal.ZERO;

            for (int i = 0; i < todosLosTramo.size() - 1; i++) {
                Tramo actual = todosLosTramo.get(i);

                boolean esTramoDeposito = actual.esOrigenDeposito() ||
                    actual.esDepositoDeposito();

                if (esTramoDeposito) {
                    Tramo siguiente = todosLosTramo.get(i + 1);

                    LocalDateTime finEstadia = actual.getFechaHoraFinReal();
                    LocalDateTime inicioSiguiente = siguiente.getFechaHoraInicioReal();

                    long horasEstadia = ChronoUnit.HOURS.between(finEstadia, inicioSiguiente);

                    if (horasEstadia > 0) {
                        long dias = (long) Math.ceil(horasEstadia / 24.0);

                        Ubicacion ubicacionDeposito = this.ubicacionesClient.getUbicacionById(actual.getDestino().getIdUbicacion());

                        BigDecimal costoDiario = ubicacionDeposito.getDeposito().getCostoEstadiaDiario();
                        BigDecimal costoEstadiaTramo = costoDiario.multiply(new BigDecimal(dias));
                        costoTotalEstadiasReales = costoTotalEstadiasReales.add(costoEstadiaTramo);

                        log.debug("Estadia en deposito", 
                            kv("nombre_deposito", ubicacionDeposito.getDeposito().getNombre()), 
                            kv("dias", dias), 
                            kv("costo_diario", costoDiario), 
                            kv("costo_estadia_tramo", costoEstadiaTramo)
                        );
                    }

                }
            }

            BigDecimal cargoFijoUnitario = ruta.getCargosGestionFijo() != null ? ruta.getCargosGestionFijo() : BigDecimal.ZERO;
            BigDecimal cargosFijosTotales = cargoFijoUnitario.multiply(new BigDecimal(todosLosTramo.size()));

            tarifa.calcularCostoFinal(
                distanciaTotal,
                cargosFijosTotales,
                costoTotalEstadiasReales,
                preciosNegocio
            );

            log.info("Costo final calculado para Solicitud", 
                kv("solicitud_id", solicitudTraslado.getIdSolicitud()), 
                kv("costo_final", tarifa.getCostoFinal())
            );

            LocalDateTime inicio = todosLosTramo.getFirst().getFechaHoraInicioReal();
            LocalDateTime fin = tramo.getFechaHoraFinReal();

            if(inicio == null || fin == null) {
                throw new ServiceError("", Errores.TRAMO_NO_ENCONTRADO, 404);
            }

            Duration duration = Duration.between(inicio, fin);
            BigDecimal segundos = BigDecimal.valueOf(duration.getSeconds());
            BigDecimal tiempoRealHoras = segundos.divide(BigDecimal.valueOf(3600), 2, RoundingMode.HALF_UP);

            Long idSolicitud = solicitudTraslado.getIdSolicitud();

            SolicitudEditRequest editRequest = SolicitudEditRequest.builder()
                .tiempoRealHoras(tiempoRealHoras)
                .tarifa(tarifa)
                .build();

            this.enviosClient.editSolicitud(idSolicitud, editRequest);;

            this.enviosClient.cambioDeEstadoSolicitud(
                idSolicitud,
                EstadoSolicitud.ENTREGADO.name(),
                Descripcciones.FINALIZADA.getDescripccion()
            );

            log.debug("Solicitud actualizada a ENTREGADO", 
                kv("solicitud_id", solicitudTraslado.getIdSolicitud())
            );
        }

        log.info("Registro de FIN de TramoModel completado", 
            kv("tramo_id", request.getIdTramo())
        );
    }

}

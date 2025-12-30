package backend.grupo130.envios.service;

import backend.grupo130.envios.client.contenedores.ContenedorClient;
import backend.grupo130.envios.client.contenedores.entity.Contenedor;
import backend.grupo130.envios.config.enums.Descripcciones;
import backend.grupo130.envios.config.enums.Errores;
import backend.grupo130.envios.config.enums.EstadoSolicitud;
import backend.grupo130.envios.config.exceptions.ServiceError;
import backend.grupo130.envios.data.entity.SeguimientoEnvio;
import backend.grupo130.envios.data.entity.SolicitudTraslado;
import backend.grupo130.envios.data.entity.Tarifa;
import backend.grupo130.envios.dto.solicitud.SolicitudMapperDto;
import backend.grupo130.envios.dto.solicitud.request.*;
import backend.grupo130.envios.dto.solicitud.response.SolicitudCambioDeEstadoResponse;
import backend.grupo130.envios.dto.solicitud.response.SolicitudEditResponse;
import backend.grupo130.envios.dto.solicitud.response.SolicitudGetAllResponse;
import backend.grupo130.envios.dto.solicitud.response.SolicitudGetByIdResponse;
import backend.grupo130.envios.repository.SolicitudTrasladoRepository;
import backend.grupo130.envios.repository.TarifaRepository;
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
public class SolicitudService {

    private final SolicitudTrasladoRepository solicitudRepository;

    private final ContenedorClient contenedorClient;

    private final TarifaRepository tarifaRepository;

    public SolicitudGetByIdResponse getById(SolicitudGetByIdRequest request) throws ServiceError {
        log.info("Inicio getById. Buscando solicitud con ID: {}", request.getIdSolicitud());

        SolicitudTraslado solicitud = this.solicitudRepository.getById(request.getIdSolicitud());

        if (solicitud == null) {
            throw new ServiceError("", Errores.SOLICITUD_NO_ENCONTRADA, 404);
        }

        log.info("Solicitud encontrada exitosamente. ID: {}", request.getIdSolicitud());

        return SolicitudMapperDto.toSolicitudGetByIdResponse(solicitud);
    }

    public SolicitudGetAllResponse getAll() throws ServiceError {
        log.info("Inicio getAll. Buscando todas las solicitudes.");

        List<SolicitudTraslado> solicitudes = this.solicitudRepository.getAll();
        log.info("Se encontraron {} solicitudes.", solicitudes.size());

        return SolicitudMapperDto.toSolicitudGetAllResponse(solicitudes);
    }

    public SolicitudGetByIdResponse register(SolicitudRegisterRequest request) throws ServiceError {
        log.info("Inicio register. Registrando nueva solicitud para cliente ID: {}", request.getIdCliente());

        log.info("Buscando contenedor existente para cliente {}...", request.getIdCliente());

        Contenedor contenedor = this.contenedorClient.getByPesoVolumen(
            request.getPeso(),
            request.getVolumen()
        );

        if (contenedor != null) {
            log.info("Contenedor encontrado. ID: {}", contenedor.getIdContenedor());

            this.contenedorClient.asignarCliente(contenedor.getIdContenedor(), request.getIdCliente());
        } else {
            log.warn("Contenedor no encontrado (404). Creando uno nuevo...");

            Long nuevoId = this.contenedorClient.register(
                request.getPeso(),
                request.getVolumen(),
                request.getIdCliente()
            );

            log.info("Volviendo a buscar el contenedor recién creado...");

            contenedor = this.contenedorClient.getById(nuevoId);

            log.info("Contenedor creado y recuperado. ID: {}", contenedor.getIdContenedor());
        }

        SolicitudTraslado solicitud = new SolicitudTraslado();

        solicitud.setIdContenedor(contenedor.getIdContenedor());
        solicitud.setIdCliente(request.getIdCliente());
        solicitud.setEstado(EstadoSolicitud.BORRADOR);

        SeguimientoEnvio primerSeguimiento = new SeguimientoEnvio();

        primerSeguimiento.setFechaHoraInicio(LocalDateTime.now());
        primerSeguimiento.setEstado(EstadoSolicitud.BORRADOR);
        primerSeguimiento.setDescripcion(Descripcciones.CREADA.getDescripccion());

        List<SeguimientoEnvio> inicio = List.of(primerSeguimiento);

        solicitud.setSeguimientos(inicio);
        SolicitudTraslado solicitudGuardada = this.solicitudRepository.save(solicitud);

        log.info("Solicitud registrada exitosamente. Nueva ID: {}", solicitudGuardada.getIdSolicitud());
        return SolicitudMapperDto.toSolicitudGetByIdResponse(solicitudGuardada);
    }

    public SolicitudEditResponse edit(SolicitudEditRequest request) throws ServiceError {
        log.info("Inicio edit. Editando solicitud ID: {}", request.getIdSolicitud());

        SolicitudTraslado solicitud = this.solicitudRepository.getById(request.getIdSolicitud());

        if (solicitud == null) {
            throw new ServiceError("", Errores.SOLICITUD_NO_ENCONTRADA, 404);
        }

        if (request.getFechaInicio() != null) {
            solicitud.setFechaInicio(request.getFechaInicio());
        }
        if (request.getFechaFin() != null) {
            solicitud.setFechaFin(request.getFechaFin());
        }
        if (request.getTiempoEstimadoHoras() != null) {
            solicitud.setTiempoEstimadoHoras(request.getTiempoEstimadoHoras());
        }
        if (request.getTiempoRealHoras() != null) {
            solicitud.setTiempoRealHoras(request.getTiempoRealHoras());
        }
        if (request.getIdOrigen() != null) {
            solicitud.setIdOrigen(request.getIdOrigen());
        }
        if (request.getIdDestino() != null) {
            solicitud.setIdDestino(request.getIdDestino());
        }
        if (request.getCostoEstimado() != null) {
            solicitud.setCostoEstimado(request.getCostoEstimado());
        }
        if (request.getCostoFinal() != null) {
            solicitud.setCostoFinal(request.getCostoFinal());
        }

        if (request.getTarifa() != null) {
            log.info("Procesando tarifa para solicitud {}", solicitud.getIdSolicitud());

            Tarifa tarifaRequest = request.getTarifa();

            Tarifa tarifaExistente = solicitud.getTarifa();
            if (tarifaExistente != null) {

                log.info("Actualizando tarifa existente ID: {}", tarifaExistente.getIdTarifa());

                tarifaExistente.setPesoMax(tarifaRequest.getPesoMax());
                tarifaExistente.setVolumenMax(tarifaRequest.getVolumenMax());
                tarifaExistente.setCostoBase(tarifaRequest.getCostoBase());
                tarifaExistente.setConsumoAprox(tarifaRequest.getConsumoAprox());
                tarifaExistente.setValorLitro(tarifaRequest.getValorLitro());
                tarifaExistente.setCostoEstadia(tarifaRequest.getCostoEstadia());

                this.tarifaRepository.update(tarifaExistente);
            } else {
                log.info("Guardando nueva tarifa para la solicitud");

                Tarifa tarifaGuardada = this.tarifaRepository.save(tarifaRequest);
                solicitud.setTarifa(tarifaGuardada);
            }
        }

        SolicitudTraslado solicitudActualizada = this.solicitudRepository.update(solicitud);

        log.info("Solicitud editada exitosamente. ID: {}", solicitudActualizada.getIdSolicitud());
        return SolicitudMapperDto.toSolicitudEditResponse(solicitudActualizada);
    }

    public SolicitudCambioDeEstadoResponse cambioDeEstado(SolicitudCambioDeEstadoRequest request) throws ServiceError {
        log.info("Inicio cambioDeEstado. Solicitud ID: {}. Nuevo estado: {}", request.getIdSolicitud(), request.getNuevoEstado());

        SolicitudTraslado solicitud = this.solicitudRepository.getById(request.getIdSolicitud());

        if (solicitud == null) {
            throw new ServiceError("", Errores.SOLICITUD_NO_ENCONTRADA, 404);
        }

        EstadoSolicitud nuevoEstado = EstadoSolicitud.fromString(request.getNuevoEstado());
        if (nuevoEstado == null) {
            throw new ServiceError("", Errores.ESTADO_INVALIDO, 400);
        }

        solicitud.transicionarEstado(nuevoEstado, request.getDescripcion());

        SolicitudTraslado solicitudActualizada = this.solicitudRepository.update(solicitud);
        SeguimientoEnvio seguimientoSolicitudActualizada = solicitudActualizada.getSeguimientos().getLast();

        return SolicitudMapperDto.toSolicitudCambioDeEstadoResponse(solicitudActualizada, seguimientoSolicitudActualizada);
    }

    public SolicitudCambioDeEstadoResponse confirmarSolicitud(SolicitudConfirmarRuta request) throws ServiceError {
        log.info("Confirmando solicitud . Solicitud ID: {}", request.getIdSolicitud());

        SolicitudTraslado solicitud = this.solicitudRepository.getById(request.getIdSolicitud());

        if (solicitud == null) {
            throw new ServiceError("", Errores.SOLICITUD_NO_ENCONTRADA, 404);
        }

        solicitud.transicionarEstado(EstadoSolicitud.CONFIRMADA, Descripcciones.CONFIRMADA.getDescripccion());

        SolicitudTraslado solicitudActualizada = this.solicitudRepository.update(solicitud);
        SeguimientoEnvio seguimientoSolicitudActualizada = solicitudActualizada.getSeguimientos().getLast();

        return SolicitudMapperDto.toSolicitudCambioDeEstadoResponse(solicitudActualizada, seguimientoSolicitudActualizada);
    }

}

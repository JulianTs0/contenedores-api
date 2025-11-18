package backend.grupo130.envios.service;

import backend.grupo130.envios.client.contenedores.models.Contenedor;
import backend.grupo130.envios.config.enums.Errores;
import backend.grupo130.envios.config.enums.Estado;
import backend.grupo130.envios.config.exceptions.ServiceError;
import backend.grupo130.envios.data.models.SeguimientoEnvio;
import backend.grupo130.envios.data.models.SolicitudTraslado;
import backend.grupo130.envios.data.models.Tarifa;
import backend.grupo130.envios.dto.solicitud.SolicitudMapperDto;
import backend.grupo130.envios.dto.solicitud.request.SolicitudCambioDeEstadoRequest;
import backend.grupo130.envios.dto.solicitud.request.SolicitudEditRequest;
import backend.grupo130.envios.dto.solicitud.request.SolicitudGetByIdRequest;
import backend.grupo130.envios.dto.solicitud.request.SolicitudRegisterRequest;
import backend.grupo130.envios.dto.solicitud.response.SolicitudCambioDeEstadoResponse;
import backend.grupo130.envios.dto.solicitud.response.SolicitudEditResponse;
import backend.grupo130.envios.dto.solicitud.response.SolicitudGetAllResponse;
import backend.grupo130.envios.dto.solicitud.response.SolicitudGetByIdResponse;
import backend.grupo130.envios.repository.ContenedorRepository;
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
@Slf4j // Anotación de Lombok para SLF4J
public class SolicitudService {

    private final SolicitudTrasladoRepository solicitudRepository;

    private final ContenedorRepository contenedorRepository; // Inyectamos el repo de Contenedor

    private final TarifaRepository tarifaRepository;


    public SolicitudGetByIdResponse getById(SolicitudGetByIdRequest request) throws ServiceError {
        // LOG Nivel INFO: Evento importante de flujo
        log.info("Inicio getById. Buscando solicitud con ID: {}", request.getIdSolicitud());
        try {
            SolicitudTraslado solicitud = this.solicitudRepository.getById(request.getIdSolicitud());

            if (solicitud == null) {
                // LOG Nivel WARN: Condición anómala no crítica
                log.warn("Solicitud no encontrada con ID: {}", request.getIdSolicitud());
                throw new ServiceError("", Errores.SOLICITUD_NO_ENCONTRADA, 404);
            }

            log.info("Solicitud encontrada exitosamente. ID: {}", request.getIdSolicitud());

            // --- ADAPTACIÓN MANUAL ---
            // (Lógica de negocio sin cambios)
            return new SolicitudGetByIdResponse(
                solicitud.getIdSolicitud(),
                solicitud.getFechaInicio(),
                solicitud.getFechaFin(),
                solicitud.getEstado().name(),
                solicitud.getTarifa(),
                solicitud.getSeguimientos(),
                solicitud.getIdContenedor(),
                solicitud.getIdCliente(),
                solicitud.getIdOrigen(),
                solicitud.getIdDestino(),
                solicitud.getCostoEstimado(),
                solicitud.getCostoFinal(),
                solicitud.getTiempoEstimadoHoras(),
                solicitud.getTiempoRealHoras()
            );

        } catch (ServiceError ex) {
            // Logueamos el error de servicio (controlado)
            log.warn("ServiceError en getById: {} - {}", ex.getHttpCode(), ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            // LOG Nivel ERROR: Fallo grave o excepción no manejada
            // Incluimos la traza de la excepción (ex) como buena práctica
            log.error("Error interno al buscar solicitud por ID {}: {}", request.getIdSolicitud(), ex.getMessage(), ex);
            throw new ServiceError(ex.getMessage(), Errores.ERROR_INTERNO, 500);
        }
    }


    public SolicitudGetAllResponse getAll() throws ServiceError {
        log.info("Inicio getAll. Buscando todas las solicitudes.");
        try {

            List<SolicitudTraslado> solicitudes = this.solicitudRepository.getAll();
            log.info("Se encontraron {} solicitudes.", solicitudes.size());

            return SolicitudMapperDto.toResponseGet(solicitudes);
        } catch (ServiceError ex) {
            log.warn("ServiceError en getAll: {} - {}", ex.getHttpCode(), ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            log.error("Error interno al buscar todas las solicitudes: {}", ex.getMessage(), ex);
            throw new ServiceError(ex.getMessage(), Errores.ERROR_INTERNO, 500);
        }
    }

    public SolicitudGetByIdResponse register(SolicitudRegisterRequest request) throws ServiceError {
        log.info("Inicio register. Registrando nueva solicitud para cliente ID: {}", request.getIdCliente());
        try {

            Contenedor contenedor;
            try {
                log.info("Buscando contenedor existente para cliente {}...", request.getIdCliente());
                contenedor = this.contenedorRepository.getByPesoVolumen(
                    request.getPeso(),
                    request.getVolumen()
                );

                if(contenedor.getIdCliente() != null){
                    throw new ServiceError("[404]",Errores.CONTENEDOR_NO_ENCONTRADO,404);
                }

                this.contenedorRepository.asignarCliente(contenedor.getIdContenedor(), request.getIdCliente());
                log.info("Contenedor encontrado. ID: {}", contenedor.getIdContenedor());

            } catch (ServiceError ex) {
                if (ex.getMessage() != null && ex.getMessage().contains("[404]")) {
                    log.warn("Contenedor no encontrado (404). Creando uno nuevo...");
                    Long nuevoId = this.contenedorRepository.register(
                        request.getPeso(),
                        request.getVolumen(),
                        request.getIdCliente()
                    );
                    log.info("Volviendo a buscar el contenedor recién creado...");
                    contenedor = this.contenedorRepository.getById(nuevoId);
                    log.info("Contenedor creado y recuperado. ID: {}", contenedor.getIdContenedor());
                } else {
                    // Relanzamos si es otro tipo de ServiceError
                    throw ex;
                }
            }

            // (Lógica de negocio sin cambios)
            SolicitudTraslado solicitud = new SolicitudTraslado();
            solicitud.setIdContenedor(contenedor.getIdContenedor());
            solicitud.setIdCliente(request.getIdCliente());
            solicitud.setEstado(Estado.BORRADOR);

            SeguimientoEnvio primerSeguimiento = new SeguimientoEnvio(
                null,
                LocalDateTime.now(),
                null,
                Estado.BORRADOR,
                "Solicitud creada."
            );

            List<SeguimientoEnvio> inicio = List.of(primerSeguimiento);
            solicitud.setSeguimientos(inicio);
            SolicitudTraslado solicitudGuardada = this.solicitudRepository.save(solicitud);

            log.info("Solicitud registrada exitosamente. Nueva ID: {}", solicitudGuardada.getIdSolicitud());
            return SolicitudMapperDto.toResponseGet(solicitudGuardada);

        } catch (ServiceError ex) {
            log.warn("ServiceError en register: {} - {}", ex.getHttpCode(), ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            log.error("Error interno al registrar solicitud: {}", ex.getMessage(), ex);
            throw new ServiceError(ex.getMessage(), Errores.ERROR_INTERNO, 500);
        }
    }


    public SolicitudEditResponse edit(SolicitudEditRequest request) throws ServiceError {
        log.info("Inicio edit. Editando solicitud ID: {}", request.getIdSolicitud());
        try {
            SolicitudTraslado solicitud = this.solicitudRepository.getById(request.getIdSolicitud());
            if (solicitud == null) {
                log.warn("Solicitud no encontrada para editar. ID: {}", request.getIdSolicitud());
                throw new ServiceError("", Errores.SOLICITUD_NO_ENCONTRADA, 404);
            }

            // (Lógica de negocio sin cambios)
            if (solicitud.getEstado() == Estado.ENTREGADO) {
                log.warn("Intento de edición en solicitud finalizada. ID: {}", request.getIdSolicitud());
                throw new ServiceError("No se puede editar una solicitud FINALIZADA", Errores.TRANSICION_ESTADO_INVALIDA, 400);
            }

            // (Lógica de negocio sin cambios)
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

            // --- Lógica para Tarifa (Sin cambios) ---
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
            return SolicitudMapperDto.toResponsePatch(solicitudActualizada);

        } catch (ServiceError ex) {
            log.warn("ServiceError en edit: {} - {}", ex.getHttpCode(), ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            log.error("Error interno al editar solicitud ID {}: {}", request.getIdSolicitud(), ex.getMessage(), ex);
            throw new ServiceError(ex.getMessage(), Errores.ERROR_INTERNO, 500);
        }
    }

    // CAMBIO DE ESTADO
    public SolicitudCambioDeEstadoResponse cambioDeEstado(SolicitudCambioDeEstadoRequest request) throws ServiceError {
        log.info("Inicio cambioDeEstado. Solicitud ID: {}. Nuevo estado: {}", request.getIdSolicitud(), request.getNuevoEstado());
        try {

            SolicitudTraslado solicitud = this.solicitudRepository.getById(request.getIdSolicitud());

            if (solicitud == null) {
                log.warn("Solicitud no encontrada para cambio de estado. ID: {}", request.getIdSolicitud());
                throw new ServiceError("", Errores.SOLICITUD_NO_ENCONTRADA, 404);
            }

            Estado nuevoEstado = Estado.fromString(request.getNuevoEstado());
            if (nuevoEstado == null) {
                log.warn("Estado inválido proporcionado: {}", request.getNuevoEstado());
                throw new ServiceError("", Errores.ESTADO_INVALIDO, 400);
            }

            // (Lógica de negocio sin cambios)
            switch (solicitud.getEstado()) {
                case BORRADOR:
                    if (nuevoEstado != Estado.PROGRAMADO) {
                        throw new ServiceError("", Errores.TRANSICION_ESTADO_INVALIDA, 400);
                    }
                    break;
                case PROGRAMADO:
                    if (nuevoEstado != Estado.EN_TRANSITO) {
                        throw new ServiceError("", Errores.TRANSICION_ESTADO_INVALIDA, 400);
                    }
                    break;
                case EN_TRANSITO:
                    if (nuevoEstado != Estado.ENTREGADO) {
                        throw new ServiceError("", Errores.TRANSICION_ESTADO_INVALIDA, 400);
                    }
                    break;
                case ENTREGADO:
                    log.warn("Intento de cambiar estado en solicitud ya finalizada. ID: {}", request.getIdSolicitud());
                    throw new ServiceError("", Errores.SOLICITUD_YA_FINALIZADA, 400);
            }

            // (Lógica de negocio sin cambios)
            if(nuevoEstado.equals(Estado.EN_TRANSITO)){
                solicitud.setFechaInicio(LocalDateTime.now());
            }
            if(nuevoEstado.equals(Estado.ENTREGADO)){
                solicitud.setFechaFin(LocalDateTime.now());
            }

            solicitud.setEstado(nuevoEstado);

            SeguimientoEnvio nuevoSeguimiento = new SeguimientoEnvio(
                null,
                LocalDateTime.now(),
                (nuevoEstado == Estado.ENTREGADO) ? LocalDateTime.now() : null,
                nuevoEstado,
                request.getDescripcion()
            );

            solicitud.getSeguimientos().getLast().setFechaHoraFin(LocalDateTime.now());
            solicitud.getSeguimientos().add(nuevoSeguimiento);

            SolicitudTraslado solicitudActualizada = this.solicitudRepository.update(solicitud);

            SeguimientoEnvio seguimientoSolicitudActualizada = solicitudActualizada.getSeguimientos().get(solicitudActualizada.getSeguimientos().size() - 1);

            log.info("Cambio de estado exitoso. Solicitud ID: {}. Nuevo estado: {}", request.getIdSolicitud(), nuevoEstado);
            return SolicitudMapperDto.toResponsePatch(solicitudActualizada, seguimientoSolicitudActualizada);

        } catch (ServiceError ex) {
            log.warn("ServiceError en cambioDeEstado (ID: {}): {} - {}", request.getIdSolicitud(), ex.getHttpCode(), ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            log.error("Error interno en cambioDeEstado (ID: {}): {}", request.getIdSolicitud(), ex.getMessage(), ex);
            throw new ServiceError(ex.getMessage(), Errores.ERROR_INTERNO, 500);
        }
    }
}

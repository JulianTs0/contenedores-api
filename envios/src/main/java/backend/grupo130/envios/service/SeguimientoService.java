package backend.grupo130.envios.service;

import backend.grupo130.envios.config.enums.Errores;
import backend.grupo130.envios.config.enums.Estado;
import backend.grupo130.envios.config.exceptions.ServiceError;
import backend.grupo130.envios.data.models.SeguimientoEnvio;
import backend.grupo130.envios.data.models.SolicitudTraslado;
import backend.grupo130.envios.dto.seguimiento.SeguimientoMapperDto;
import backend.grupo130.envios.dto.seguimiento.request.SeguimientoGetByIdRequest;
import backend.grupo130.envios.dto.seguimiento.request.SeguimientoRegisterRequest;
import backend.grupo130.envios.dto.seguimiento.response.SeguimientoGetAllResponse;
import backend.grupo130.envios.dto.seguimiento.response.SeguimientoGetByIdResponse;
import backend.grupo130.envios.repository.SeguimientoEnvioRepository;
import backend.grupo130.envios.repository.SolicitudTrasladoRepository;
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
public class SeguimientoService {

    private final SeguimientoEnvioRepository seguimientoRepository;

    private final SolicitudTrasladoRepository solicitudRepository;


    public SeguimientoGetByIdResponse getById(SeguimientoGetByIdRequest request) throws ServiceError {
        log.info("Inicio getById. Buscando seguimiento con ID: {}", request.getIdSeguimiento());
        try {
            SeguimientoEnvio seguimiento = this.seguimientoRepository.getById(request.getIdSeguimiento());

            if (seguimiento == null) {
                log.warn("Seguimiento no encontrado con ID: {}", request.getIdSeguimiento());
                throw new ServiceError("", Errores.SEGUIMIENTO_NO_ENCONTRADO, 404);
            }

            log.info("Seguimiento encontrado exitosamente. ID: {}", request.getIdSeguimiento());
            SeguimientoGetByIdResponse response = SeguimientoMapperDto.toResponseGet(seguimiento);
            return response;

        } catch (ServiceError ex) {
            log.warn("ServiceError en getById (Seguimiento ID: {}): {} - {}", request.getIdSeguimiento(), ex.getHttpCode(), ex.getMessage());
            throw ex;
        }
        catch (Exception ex) {
            log.error("Error interno al buscar seguimiento por ID {}: {}", request.getIdSeguimiento(), ex.getMessage(), ex);
            throw new ServiceError(ex.getMessage() , Errores.ERROR_INTERNO, 500);
        }
    }

    public SeguimientoGetAllResponse getAll() throws ServiceError {
        log.info("Inicio getAll. Buscando todos los seguimientos.");
        try {
            List<SeguimientoEnvio> seguimientos = this.seguimientoRepository.getAll();
            log.info("Se encontraron {} seguimientos.", seguimientos.size());
            SeguimientoGetAllResponse response = SeguimientoMapperDto.toResponseGet(seguimientos);
            return response;
        } catch (ServiceError ex) {
            log.warn("ServiceError en getAll (Seguimientos): {} - {}", ex.getHttpCode(), ex.getMessage());
            throw ex;
        }  catch (Exception ex) {
            log.error("Error interno al buscar todos los seguimientos: {}", ex.getMessage(), ex);
            throw new ServiceError(ex.getMessage() , Errores.ERROR_INTERNO, 500);
        }
    }

    public void register(SeguimientoRegisterRequest request) throws ServiceError {
        log.info("Inicio register. Registrando nuevo seguimiento para Solicitud ID: {}", request.getIdSolicitud());
        try {

            SolicitudTraslado solicitud = solicitudRepository.getById(request.getIdSolicitud());

            if (solicitud == null) {
                log.warn("Solicitud no encontrada (ID: {}) al registrar seguimiento.", request.getIdSolicitud());
                throw new ServiceError("", Errores.SOLICITUD_NO_ENCONTRADA, 404);
            }

            Estado estado = Estado.fromString(request.getEstado());
            if (estado == null) {
                log.warn("Estado inválido proporcionado: {}", request.getEstado());
                throw new ServiceError("", Errores.ESTADO_INVALIDO, 400);
            }

            SeguimientoEnvio seguimiento = new SeguimientoEnvio();
            seguimiento.setDescripcion(request.getDescripcion());
            seguimiento.setFechaHoraInicio(LocalDateTime.now());
            seguimiento.setEstado(estado);
            solicitud.getSeguimientos().add(seguimiento);

            this.solicitudRepository.update(solicitud);
            log.info("Seguimiento registrado exitosamente para Solicitud ID: {}", request.getIdSolicitud());

        } catch (ServiceError ex) {
            log.warn("ServiceError en register (Seguimiento para Solicitud ID: {}): {} - {}", request.getIdSolicitud(), ex.getHttpCode(), ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            log.error("Error interno al registrar seguimiento (Solicitud ID: {}): {}", request.getIdSolicitud(), ex.getMessage(), ex);
            throw new ServiceError(ex.getMessage() , Errores.ERROR_INTERNO, 500);
        }
    }

}

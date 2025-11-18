package backend.grupo130.tramos.repository;

import backend.grupo130.tramos.client.envios.EnvioClient;
import backend.grupo130.tramos.client.envios.models.SeguimientoEnvio;
import backend.grupo130.tramos.client.envios.models.SolicitudTraslado;
import backend.grupo130.tramos.client.envios.models.Tarifa;
import backend.grupo130.tramos.client.envios.request.SolicitudCambioDeEstadoRequest;
import backend.grupo130.tramos.client.envios.request.SolicitudEditRequest;
import backend.grupo130.tramos.client.envios.responses.*;
import backend.grupo130.tramos.config.enums.Errores;
import backend.grupo130.tramos.config.exceptions.ServiceError;
import feign.FeignException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class EnviosRepository {

    private final EnvioClient envioClient;

    public Tarifa getTarifaById(Long tarifaId) {
        try {
            return new Tarifa();

        } catch (ServiceError ex) {
            throw ex;
        } catch (Exception ex){
            throw new ServiceError(ex.getMessage(), Errores.ERROR_INTERNO , 500);
        }
    }

    public SolicitudTraslado getSolicitudTrasladoById(Long solicitudTrasladoId) {
        try {
            SolicitudGetByIdResponse response = this.envioClient.getSolicitudTrasladoById(solicitudTrasladoId);

            return new SolicitudTraslado(
                response.getIdSolicitud(),
                response.getFechaInicio(),
                response.getFechaFin(),
                response.getTiempoEstimadoHoras(),
                response.getTiempoRealHoras(),
                response.getCostoEstimado(),
                response.getCostoFinal(),
                response.getTarifa(),
                response.getSeguimientos(),
                response.getIdContenedor(),
                response.getIdCliente(),
                response.getIdOrigen(),
                response.getIdDestino()
            );

        } catch (ServiceError ex) {
            throw ex;
        } catch (Exception ex){
            throw new ServiceError(ex.getMessage(), Errores.ERROR_INTERNO , 500);
        }
    }

    public SeguimientoEnvio getSeguimientoEnvioById(Long seguimientoEnvioId) {
        try {

            return new SeguimientoEnvio();

        } catch (ServiceError ex) {
            throw ex;
        } catch (Exception ex){
            throw new ServiceError(ex.getMessage(), Errores.ERROR_INTERNO , 500);
        }
    }

    public SolicitudEditResponse editSolicitud(SolicitudEditRequest request) {
        try {

            return this.envioClient.editSolicitud(request);

        } catch (FeignException ex) {
            throw new ServiceError(ex.contentUTF8(), Errores.ERROR_INTERNO , ex.status());
        } catch (Exception ex){
            throw new ServiceError(ex.getMessage(), Errores.ERROR_INTERNO , 500);
        }
    }


    public SolicitudCambioDeEstadoResponse cambioDeEstadoSolicitud(SolicitudCambioDeEstadoRequest request) {
        try {
            return this.envioClient.cambioDeEstadoSolicitud(request);

        } catch (Exception ex){

            throw new ServiceError(ex.getMessage(), Errores.ERROR_INTERNO , 500);
        }
    }
}

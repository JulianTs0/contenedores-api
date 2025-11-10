package backend.grupo130.tramos.repository;

import backend.grupo130.tramos.client.envios.EnvioClient;
import backend.grupo130.tramos.client.envios.models.SeguimientoEnvio;
import backend.grupo130.tramos.client.envios.models.SolicitudTraslado;
import backend.grupo130.tramos.client.envios.models.Tarifa;
import backend.grupo130.tramos.client.envios.responses.GetSeguimientoEnvioByIdResponse;
import backend.grupo130.tramos.client.envios.responses.GetSolicitudTrasladoByIdResponse;
import backend.grupo130.tramos.client.envios.responses.GetTarifaByIdResponse;
import backend.grupo130.tramos.config.exceptions.ServiceError;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class EnviosRepository {

    private final EnvioClient envioClient;

    public Tarifa getTarifaById(Integer tarifaId) {
        try {
            GetTarifaByIdResponse response = this.envioClient.getTarifaById(tarifaId);
            return new Tarifa();

        } catch (Exception ex) {
            throw new ServiceError("Error interno", 500);
        }
    }

    public SolicitudTraslado getSolicitudTrasladoById(Integer solicitudTrasladoId) {
        try {
            GetSolicitudTrasladoByIdResponse response = this.envioClient.getSolicitudTrasladoById(solicitudTrasladoId);
            return new SolicitudTraslado();

        } catch (Exception ex) {
            throw new ServiceError("Error interno", 500);
        }
    }

    public SeguimientoEnvio getSeguimientoEnvioById(Integer seguimientoEnvioId) {
        try {
            GetSeguimientoEnvioByIdResponse response = this.envioClient.getSeguimientoEnvioById(seguimientoEnvioId);
            return new SeguimientoEnvio();

        } catch (Exception ex) {
            throw new ServiceError("Error interno", 500);
        }
    }
}

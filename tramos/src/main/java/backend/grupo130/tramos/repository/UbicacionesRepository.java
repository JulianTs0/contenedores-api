package backend.grupo130.tramos.repository;

import backend.grupo130.tramos.client.ubicaciones.UbicacionClient;
import backend.grupo130.tramos.client.ubicaciones.models.Deposito;
import backend.grupo130.tramos.client.ubicaciones.models.Ubicacion;
import backend.grupo130.tramos.client.ubicaciones.responses.GetDepositoByIdResponse;
import backend.grupo130.tramos.client.ubicaciones.responses.GetUbicacionByIdResponse;
import backend.grupo130.tramos.client.ubicaciones.responses.GetUbicacionGetAllResponse;
import backend.grupo130.tramos.config.exceptions.ServiceError;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@AllArgsConstructor
@Slf4j
public class UbicacionesRepository {

    private final UbicacionClient ubicacionClient;

    public Deposito getDepositoById(Integer depositoId) {
        try {
            GetDepositoByIdResponse response = this.ubicacionClient.getDepositoById(depositoId);

            Ubicacion ubicacion = this.getUbicacionById(response.getIdUbicacion());

            Deposito deposito = new Deposito(
                response.getIdDeposito(),
                response.getNombre(),
                response.getCostoEstadiaDiario(),
                ubicacion
            );

            return deposito;

        } catch (Exception ex) {
            log.warn(ex.getMessage());
            ex.printStackTrace();
            throw new ServiceError("Error interno", 500);
        }
    }

    public Ubicacion getUbicacionById(Integer ubicacionId) {
        try {

            GetUbicacionByIdResponse response = this.ubicacionClient.getUbicacionById(ubicacionId);

            Ubicacion ubicacion = new Ubicacion(
                response.getUbicacionId(),
                response.getDireccion(),
                response.getLatitud(),
                response.getLongitud(),
                response.getIdDeposito()
            );

            return ubicacion;

        } catch (Exception ex) {
            log.warn(ex.getMessage());
            ex.printStackTrace();
            throw new ServiceError("Error interno", 500);
        }
    }

    public List<Ubicacion> getUbicacionAll() {
        try {

            GetUbicacionGetAllResponse response = this.ubicacionClient.getUbicacionAll();

            return response.getUbicaciones();

        } catch (Exception ex) {
            throw new ServiceError("Error interno", 500);
        }
    }

}

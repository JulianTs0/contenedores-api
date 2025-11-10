package backend.grupo130.tramos.repository;

import backend.grupo130.tramos.client.ubicaciones.UbicacionClient;
import backend.grupo130.tramos.client.ubicaciones.models.Deposito;
import backend.grupo130.tramos.client.ubicaciones.models.Ubicacion;
import backend.grupo130.tramos.client.ubicaciones.responses.GetDepositoByIdResponse;
import backend.grupo130.tramos.client.ubicaciones.responses.GetUbicacionByIdResponse;
import backend.grupo130.tramos.client.ubicaciones.responses.GetUbicacionGetAllResponse;
import backend.grupo130.tramos.config.exceptions.ServiceError;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@AllArgsConstructor
public class UbicacionesRepository {

    private final UbicacionClient ubicacionClient;

    public Deposito getDepositoById(Integer depositoId) {
        try {
            GetDepositoByIdResponse response = this.ubicacionClient.getDepositoById(depositoId);
            return new Deposito();

        } catch (Exception ex) {
            throw new ServiceError("Error interno", 500);
        }
    }

    public Ubicacion getUbicacionById(Integer ubicacionId) {
        try {
            GetUbicacionByIdResponse response = this.ubicacionClient.getUbicacionById(ubicacionId);
            return new Ubicacion();

        } catch (Exception ex) {
            throw new ServiceError("Error interno", 500);
        }
    }

    public List<Ubicacion> getUbicacionAll() {
        try {

            GetUbicacionGetAllResponse response = this.ubicacionClient.getUbicacionAll();
            return List.of();

        } catch (Exception ex) {
            throw new ServiceError("Error interno", 500);
        }
    }

}

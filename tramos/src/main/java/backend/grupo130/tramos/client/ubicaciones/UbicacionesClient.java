package backend.grupo130.tramos.client.ubicaciones;

import backend.grupo130.tramos.client.ubicaciones.entity.Deposito;
import backend.grupo130.tramos.client.ubicaciones.entity.Ubicacion;
import backend.grupo130.tramos.client.ubicaciones.responses.DepositoGetByIdResponse;
import backend.grupo130.tramos.client.ubicaciones.responses.UbicacionGetAllResponse;
import backend.grupo130.tramos.client.ubicaciones.responses.UbicacionGetByIdResponse;
import backend.grupo130.tramos.client.ubicaciones.responses.UbicacionGetListByIdResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@AllArgsConstructor
@Slf4j
public class UbicacionesClient {

    private final UbicacionesGateway ubicacionesGateway;

    public Deposito getDepositoById(Long depositoId) {

        DepositoGetByIdResponse response = this.ubicacionesGateway.getDepositoById(depositoId);

        Deposito deposito = new Deposito(
            response.getIdDeposito(),
            response.getNombre(),
            response.getCostoEstadiaDiario()
        );

        return deposito;
    }

    public Ubicacion getUbicacionById(Long ubicacionId) {

        UbicacionGetByIdResponse response = this.ubicacionesGateway.getUbicacionById(ubicacionId);

        Ubicacion ubicacion = new Ubicacion(
            response.getIdUbicacion(),
            response.getDireccion(),
            response.getLatitud(),
            response.getLongitud(),
            response.getDeposito()
        );

        return ubicacion;
    }

    public List<Ubicacion> getUbicacionAll() {

        UbicacionGetAllResponse response = this.ubicacionesGateway.getUbicacionAll();

        return response.getUbicaciones().stream()
            .map(
                dto -> {
                    return new Ubicacion(
                        dto.getIdUbicacion(),
                        dto.getDireccion(),
                        dto.getLatitud(),
                        dto.getLongitud(),
                        dto.getDeposito()
                    );
                }
            )
            .toList();
    }

    public List<Ubicacion> getByListIds(List<Long> ids) {

        UbicacionGetListByIdResponse response = this.ubicacionesGateway.getUbicacionByIds(ids);

        return response.getUbicaciones().stream()
            .map(
                dto -> {
                    return new Ubicacion(
                        dto.getIdUbicacion(),
                        dto.getDireccion(),
                        dto.getLatitud(),
                        dto.getLongitud(),
                        dto.getDeposito()
                    );
                }
            )
            .toList();
    }

}

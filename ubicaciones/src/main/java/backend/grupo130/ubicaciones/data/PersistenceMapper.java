
package backend.grupo130.ubicaciones.data;

import backend.grupo130.ubicaciones.data.entity.Deposito;
import backend.grupo130.ubicaciones.data.entity.Ubicacion;
import backend.grupo130.ubicaciones.data.models.DepositoModel;
import backend.grupo130.ubicaciones.data.models.UbicacionModel;

import java.util.List;
import java.util.stream.Collectors;

public class PersistenceMapper {

    public static Ubicacion toDomain(UbicacionModel ubicacionModel) {
        if (ubicacionModel == null) {
            return null;
        }

        Ubicacion entity = new Ubicacion();
        entity.setIdUbicacion(ubicacionModel.getIdUbicacion());
        entity.setDireccionTextual(ubicacionModel.getDireccionTextual());
        entity.setLatitud(ubicacionModel.getLatitud());
        entity.setLongitud(ubicacionModel.getLongitud());
        entity.setDeposito(toDomain(ubicacionModel.getDeposito()));

        return entity;
    }

    public static UbicacionModel toModel(Ubicacion ubicacion) {
        if (ubicacion == null) {
            return null;
        }

        UbicacionModel model = new UbicacionModel();
        model.setIdUbicacion(ubicacion.getIdUbicacion());
        model.setDireccionTextual(ubicacion.getDireccionTextual());
        model.setLatitud(ubicacion.getLatitud());
        model.setLongitud(ubicacion.getLongitud());
        model.setDeposito(toModel(ubicacion.getDeposito()));

        return model;
    }

    public static List<Ubicacion> toDomain(List<UbicacionModel> models) {
        return models.stream()
                .map(PersistenceMapper::toDomain)
                .collect(Collectors.toList());
    }

    public static List<UbicacionModel> toModel(List<Ubicacion> entities) {
        return entities.stream()
                .map(PersistenceMapper::toModel)
                .collect(Collectors.toList());
    }

    public static Deposito toDomain(DepositoModel depositoModel) {
        if (depositoModel == null) {
            return null;
        }

        Deposito entity = new Deposito();
        entity.setIdDeposito(depositoModel.getIdDeposito());
        entity.setNombre(depositoModel.getNombre());
        entity.setCostoEstadiaDiario(depositoModel.getCostoEstadiaDiario());

        return entity;
    }

    public static DepositoModel toModel(Deposito deposito) {
        if (deposito == null) {
            return null;
        }

        DepositoModel model = new DepositoModel();
        model.setIdDeposito(deposito.getIdDeposito());
        model.setNombre(deposito.getNombre());
        model.setCostoEstadiaDiario(deposito.getCostoEstadiaDiario());

        return model;
    }

    public static List<Deposito> toDomainDeposito(List<DepositoModel> models) {
        return models.stream()
                .map(PersistenceMapper::toDomain)
                .collect(Collectors.toList());
    }

    public static List<DepositoModel> toModelDeposito(List<Deposito> entities) {
        return entities.stream()
                .map(PersistenceMapper::toModel)
                .collect(Collectors.toList());
    }
}


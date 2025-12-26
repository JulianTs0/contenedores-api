package backend.grupo130.camiones.data;

import backend.grupo130.camiones.client.usuarios.entity.Usuario;
import backend.grupo130.camiones.data.entity.Camion;
import backend.grupo130.camiones.data.models.CamionModel;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PersistanceMapper {

    public static Camion toDomain(CamionModel camionModel) { // Renamed parameter
        if (camionModel == null) {
            return null;
        }

        Camion entity = new Camion();
        entity.setDominio(camionModel.getDominio());
        entity.setCapacidadPeso(camionModel.getCapacidadPeso());
        entity.setCapacidadVolumen(camionModel.getCapacidadVolumen());
        entity.setConsumoCombustible(camionModel.getConsumoCombustible());
        entity.setCostoTrasladoBase(camionModel.getCostoTrasladoBase());
        entity.setEstado(camionModel.getEstado());
        Long idTransportista = camionModel.getIdTransportista() != null ? camionModel.getIdTransportista() : null;

        entity.setTransportista(Usuario.builder().idUsuario(idTransportista).build());

        return entity;
    }

    public static CamionModel toModel(Camion camion) { // Renamed parameter
        if (camion == null) {
            return null;
        }

        CamionModel model = new CamionModel();
        model.setDominio(camion.getDominio());
        model.setCapacidadPeso(camion.getCapacidadPeso());
        model.setCapacidadVolumen(camion.getCapacidadVolumen());
        model.setConsumoCombustible(camion.getConsumoCombustible());
        model.setCostoTrasladoBase(camion.getCostoTrasladoBase());
        model.setEstado(camion.getEstado());

        Long idTransportista = camion.getTransportista() != null ? camion.getTransportista().getIdUsuario() : null;
        model.setIdTransportista(idTransportista);

        return model;
    }

    public static List<Camion> toDomain(List<CamionModel> entities) {
        return entities.stream()
                .map(PersistanceMapper::toDomain)
                .collect(Collectors.toList());
    }

    public static List<CamionModel> toModel(List<Camion> models) {
        return models.stream()
                .map(PersistanceMapper::toModel)
                .collect(Collectors.toList());
    }
}

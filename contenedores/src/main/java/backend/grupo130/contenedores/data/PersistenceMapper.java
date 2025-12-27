package backend.grupo130.contenedores.data;

import backend.grupo130.contenedores.client.usuarios.entity.Usuario;
import backend.grupo130.contenedores.data.entity.Contenedor;
import backend.grupo130.contenedores.data.models.ContenedorModel;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PersistenceMapper {

    public static Contenedor toDomain(ContenedorModel contenedorModel) {
        if (contenedorModel == null) {
            return null;
        }

        Contenedor entity = new Contenedor();
        entity.setIdContenedor(contenedorModel.getIdContenedor());
        entity.setPeso(contenedorModel.getPeso());
        entity.setVolumen(contenedorModel.getVolumen());
        entity.setEstado(contenedorModel.getEstado());

        Long idCliente = contenedorModel.getIdCliente() != null ? contenedorModel.getIdCliente() : null;
        entity.setCliente(Usuario.builder().idUsuario(idCliente).build());

        return entity;
    }

    public static ContenedorModel toModel(Contenedor contenedor) {
        if (contenedor == null) {
            return null;
        }

        ContenedorModel model = new ContenedorModel();
        model.setIdContenedor(contenedor.getIdContenedor());
        model.setPeso(contenedor.getPeso());
        model.setVolumen(contenedor.getVolumen());
        model.setEstado(contenedor.getEstado());

        Long idCliente = contenedor.getCliente() != null ? contenedor.getCliente().getIdUsuario() : null;
        model.setIdCliente(idCliente);

        return model;
    }

    public static List<Contenedor> toDomain(List<ContenedorModel> models) {
        return models.stream()
                .map(PersistenceMapper::toDomain)
                .collect(Collectors.toList());
    }

    public static List<ContenedorModel> toModel(List<Contenedor> entities) {
        return entities.stream()
                .map(PersistenceMapper::toModel)
                .collect(Collectors.toList());
    }
}

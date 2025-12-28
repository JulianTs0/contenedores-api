
package backend.grupo130.tramos.data;

import backend.grupo130.tramos.data.entity.RutaTraslado;
import backend.grupo130.tramos.data.entity.Tramo;
import backend.grupo130.tramos.data.models.RutaTrasladoModel;
import backend.grupo130.tramos.data.models.TramoModel;

import java.util.List;
import java.util.stream.Collectors;

public class PersistenceMapper {

        public static Tramo toDomain(TramoModel model) {
            if (model == null) {
                return null;
            }

            Tramo entity = new Tramo();
            entity.setIdTramo(model.getIdTramo());
            entity.setTipoTramo(model.getTipoTramo());
            entity.setEstado(model.getEstado());
            entity.setCostoAproximado(model.getCostoAproximado());
            entity.setCostoReal(model.getCostoReal());
            entity.setFechaHoraInicioEstimado(model.getFechaHoraInicioEstimado());
            entity.setFechaHoraFinEstimado(model.getFechaHoraFinEstimado());
            entity.setFechaHoraInicioReal(model.getFechaHoraInicioReal());
            entity.setFechaHoraFinReal(model.getFechaHoraFinReal());
            entity.setOrden(model.getOrden());
            entity.setDominioCamion(model.getDominioCamion());
            entity.setIdOrigen(model.getIdOrigen());
            entity.setIdDestino(model.getIdDestino());
            entity.setDistancia(model.getDistancia());

            entity.setRutaTraslado(toDomain(model.getRutaTraslado()));

            return entity;
        }

        public static TramoModel toModel(Tramo entity) {
            if (entity == null) {
                return null;
            }

            TramoModel model = new TramoModel();
            model.setIdTramo(entity.getIdTramo());
            model.setTipoTramo(entity.getTipoTramo());
            model.setEstado(entity.getEstado());
            model.setCostoAproximado(entity.getCostoAproximado());
            model.setCostoReal(entity.getCostoReal());
            model.setFechaHoraInicioEstimado(entity.getFechaHoraInicioEstimado());
            model.setFechaHoraFinEstimado(entity.getFechaHoraFinEstimado());
            model.setFechaHoraInicioReal(entity.getFechaHoraInicioReal());
            model.setFechaHoraFinReal(entity.getFechaHoraFinReal());
            model.setOrden(entity.getOrden());
            model.setDominioCamion(entity.getDominioCamion());
            model.setIdOrigen(entity.getIdOrigen());
            model.setIdDestino(entity.getIdDestino());
            model.setDistancia(entity.getDistancia());

            model.setRutaTraslado(toModel(entity.getRutaTraslado()));

            return model;
        }

        public static List<Tramo> toDomainTramo(List<TramoModel> models) {
            if (models == null) return List.of();
            return models.stream()
                .map(PersistenceMapper::toDomain)
                .collect(Collectors.toList());
        }

        public static List<TramoModel> toModelTramo(List<Tramo> entities) {
            if (entities == null) return List.of();
            return entities.stream()
                .map(PersistenceMapper::toModel)
                .collect(Collectors.toList());
        }


        public static RutaTraslado toDomain(RutaTrasladoModel model) {
            if (model == null) {
                return null;
            }

            RutaTraslado entity = new RutaTraslado();
            entity.setIdRuta(model.getIdRuta());
            entity.setCantidadTramos(model.getCantidadTramos());
            entity.setCantidadDepositos(model.getCantidadDepositos());
            entity.setCargosGestionFijo(model.getCargosGestionFijo());
            entity.setIdSolicitud(model.getIdSolicitud());

            return entity;
        }

        public static RutaTrasladoModel toModel(RutaTraslado entity) {
            if (entity == null) {
                return null;
            }

            RutaTrasladoModel model = new RutaTrasladoModel();
            model.setIdRuta(entity.getIdRuta());
            model.setCantidadTramos(entity.getCantidadTramos());
            model.setCantidadDepositos(entity.getCantidadDepositos());
            model.setCargosGestionFijo(entity.getCargosGestionFijo());
            model.setIdSolicitud(entity.getIdSolicitud());

            return model;
        }

        public static List<RutaTraslado> toDomainRuta(List<RutaTrasladoModel> models) {
            if (models == null) return List.of();
            return models.stream()
                .map(PersistenceMapper::toDomain)
                .collect(Collectors.toList());
        }

        public static List<RutaTrasladoModel> toModelRuta(List<RutaTraslado> entities) {
            if (entities == null) return List.of();
            return entities.stream()
                .map(PersistenceMapper::toModel)
                .collect(Collectors.toList());
        }

}


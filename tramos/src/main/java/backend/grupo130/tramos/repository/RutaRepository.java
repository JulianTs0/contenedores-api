package backend.grupo130.tramos.repository;

import backend.grupo130.tramos.data.PersistenceMapper;
import backend.grupo130.tramos.data.entity.RutaTraslado;
import backend.grupo130.tramos.data.models.RutaTrasladoModel;
import backend.grupo130.tramos.data.repository.PostgresRutaRepositoryI;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@AllArgsConstructor
public class RutaRepository {

    private final PostgresRutaRepositoryI rutaRepository;

    public RutaTraslado getById(Long idRuta){
        RutaTrasladoModel model = this.rutaRepository.findById(idRuta).orElse(null);
        return PersistenceMapper.toDomain(model);
    }

    public List<RutaTraslado> getAll() {
        List<RutaTrasladoModel> models = this.rutaRepository.findAll();
        return PersistenceMapper.toDomainRuta(models);
    }

    public RutaTraslado getBySolicitud(Long id){
        RutaTrasladoModel rutaTrasladoModel = this.rutaRepository.findByIdSolicitud(id);
        return PersistenceMapper.toDomain(rutaTrasladoModel);
    }

    public RutaTraslado save(RutaTraslado ruta) {
        RutaTrasladoModel model = PersistenceMapper.toModel(ruta);
        RutaTrasladoModel saved = this.rutaRepository.save(model);
        return PersistenceMapper.toDomain(saved);
    }

    public RutaTraslado update(RutaTraslado ruta) {
        RutaTrasladoModel model = PersistenceMapper.toModel(ruta);
        RutaTrasladoModel updated = this.rutaRepository.save(model);
        return PersistenceMapper.toDomain(updated);
    }

}

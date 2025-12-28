package backend.grupo130.tramos.repository;

import backend.grupo130.tramos.data.PersistenceMapper;
import backend.grupo130.tramos.data.entity.Tramo;
import backend.grupo130.tramos.data.models.TramoModel;
import backend.grupo130.tramos.data.repository.PostgresTramoRepositoryI;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@AllArgsConstructor
public class TramoRepository {

    private final PostgresTramoRepositoryI tramoRepository;

    public Tramo getById(Long idTramo){
        TramoModel model = this.tramoRepository.findById(idTramo).orElse(null);
        return PersistenceMapper.toDomain(model);
    }

    public List<Tramo> getAll() {
        List<TramoModel> models = this.tramoRepository.findAll();
        return PersistenceMapper.toDomainTramo(models);
    }

    public List<Tramo> getByDominio(String dominio){
        List<TramoModel> models = this.tramoRepository.buscarPorDominio(dominio);
        return PersistenceMapper.toDomainTramo(models);
    }

    public Tramo save(Tramo tramo) {
        TramoModel model = PersistenceMapper.toModel(tramo);
        TramoModel saved = this.tramoRepository.save(model);
        return PersistenceMapper.toDomain(saved);
    }

    public List<Tramo> saveAll(List<Tramo> tramos) {
        List<TramoModel> tramoModels = PersistenceMapper.toModelTramo(tramos);
        List<TramoModel> saved = this.tramoRepository.saveAll(tramoModels);
        return PersistenceMapper.toDomainTramo(saved);
    }

    public Tramo update(Tramo tramo) {
        TramoModel model = PersistenceMapper.toModel(tramo);
        TramoModel updated = this.tramoRepository.save(model);
        return PersistenceMapper.toDomain(updated);
    }

    public void deleteByRutaId(Long id){
        this.tramoRepository.deleteByRutaId(id);
    }

    public List<Tramo> buscarPorRuta(Long idRuta){
        List<TramoModel> tramoModels = this.tramoRepository.buscarTodosPorIdRuta(idRuta);
        return PersistenceMapper.toDomainTramo(tramoModels);
    }

}

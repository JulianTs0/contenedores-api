package backend.grupo130.envios.repository;

import backend.grupo130.envios.data.PersistenceMapper;
import backend.grupo130.envios.data.entity.PreciosNegocio;
import backend.grupo130.envios.data.models.PreciosNegocioModel;
import backend.grupo130.envios.data.repository.PostgresPreciosNegocioRepositoryI;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@AllArgsConstructor
public class PreciosNegocioRepository {

    private final PostgresPreciosNegocioRepositoryI preciosNegocioRepository;

    public PreciosNegocio getById(Long id) {
        PreciosNegocioModel model = this.preciosNegocioRepository.findById(id).orElse(null);
        return PersistenceMapper.toDomain(model);
    }

    public List<PreciosNegocio> getAll() {
        List<PreciosNegocioModel> models = this.preciosNegocioRepository.findAll();
        return PersistenceMapper.toPreciosNegocioDomainList(models);
    }

    public PreciosNegocio getLatest() {
        PreciosNegocioModel model = this.preciosNegocioRepository.findTopByOrderByFechaCreacionDesc().orElse(null);
        return PersistenceMapper.toDomain(model);
    }

    public PreciosNegocio save(PreciosNegocio preciosNegocio) {
        PreciosNegocioModel model = PersistenceMapper.toModel(preciosNegocio);
        PreciosNegocioModel saved = this.preciosNegocioRepository.save(model);
        return PersistenceMapper.toDomain(saved);
    }

    public PreciosNegocio update(PreciosNegocio preciosNegocio) {
        PreciosNegocioModel model = PersistenceMapper.toModel(preciosNegocio);
        PreciosNegocioModel updated = this.preciosNegocioRepository.save(model);
        return PersistenceMapper.toDomain(updated);
    }

    public void delete(Long id) {
        this.preciosNegocioRepository.deleteById(id);
    }
}

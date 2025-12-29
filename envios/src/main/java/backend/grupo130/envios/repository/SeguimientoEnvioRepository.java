package backend.grupo130.envios.repository;

import backend.grupo130.envios.data.PersistenceMapper;
import backend.grupo130.envios.data.entity.SeguimientoEnvio;
import backend.grupo130.envios.data.models.SeguimientoEnvioModel;
import backend.grupo130.envios.data.repository.PostgresSeguimientoEnvioRepositoryI;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@AllArgsConstructor
public class SeguimientoEnvioRepository {

    private final PostgresSeguimientoEnvioRepositoryI seguimientoRepository;
    private final PersistenceMapper persistenceMapper;

    public SeguimientoEnvio getById(Long seguimientoId){
        SeguimientoEnvioModel model = this.seguimientoRepository.findById(seguimientoId).orElse(null);
        return PersistenceMapper.toDomain(model);
    }

    public List<SeguimientoEnvio> getAll() {
        List<SeguimientoEnvioModel> models = this.seguimientoRepository.findAll();
        return PersistenceMapper.toSeguimientoEnvioDomainList(models);
    }

    public SeguimientoEnvio save(SeguimientoEnvio seguimientoEnvio) {
        SeguimientoEnvioModel model = PersistenceMapper.toModel(seguimientoEnvio);
        SeguimientoEnvioModel saved = this.seguimientoRepository.save(model);
        return PersistenceMapper.toDomain(saved);
    }

    public SeguimientoEnvio update(SeguimientoEnvio seguimientoEnvio) {
        SeguimientoEnvioModel model = PersistenceMapper.toModel(seguimientoEnvio);
        SeguimientoEnvioModel updated = this.seguimientoRepository.save(model);
        return PersistenceMapper.toDomain(updated);
    }

    public void delete(Long seguimientoId){
        this.seguimientoRepository.deleteById(seguimientoId);
    }

}
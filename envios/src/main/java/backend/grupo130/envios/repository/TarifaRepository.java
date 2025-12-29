package backend.grupo130.envios.repository;

import backend.grupo130.envios.data.PersistenceMapper;
import backend.grupo130.envios.data.entity.Tarifa;
import backend.grupo130.envios.data.models.TarifaModel;
import backend.grupo130.envios.data.repository.PostgresTarifaRepositoryI;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@AllArgsConstructor
public class TarifaRepository {

    private final PostgresTarifaRepositoryI tarifaRepository;

    public Tarifa getById(Long tarifaId){
        TarifaModel model = this.tarifaRepository.findById(tarifaId).orElse(null);
        return PersistenceMapper.toDomain(model);
    }

    public List<Tarifa> getAll() {
        List<TarifaModel> models = this.tarifaRepository.findAll();
        return models.stream().map(PersistenceMapper::toDomain).collect(Collectors.toList());
    }


    public Tarifa save(Tarifa tarifa) {
        TarifaModel model = PersistenceMapper.toModel(tarifa);
        TarifaModel saved = this.tarifaRepository.save(model);
        return PersistenceMapper.toDomain(saved);
    }

    public Tarifa update(Tarifa tarifa) {
        TarifaModel model = PersistenceMapper.toModel(tarifa);
        TarifaModel updated = this.tarifaRepository.save(model);
        return PersistenceMapper.toDomain(updated);
    }

    public void delete(Long tarifaId){
        this.tarifaRepository.deleteById(tarifaId);
    }

}
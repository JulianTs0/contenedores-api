package backend.grupo130.envios.repository;

import backend.grupo130.envios.data.models.Tarifa;
import backend.grupo130.envios.data.repository.PostgresSolicitudRepositoryI;
import backend.grupo130.envios.data.repository.PostgresTarifaRepositoryI;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@AllArgsConstructor
public class TarifaRepository {

    private final PostgresTarifaRepositoryI tarifaRepository;

    public Tarifa getById(Long tarifaId){
        Tarifa model = this.tarifaRepository.findById(tarifaId).orElse(null);
        return model;
    }

    public List<Tarifa> getAll() {
        List<Tarifa> models = this.tarifaRepository.findAll();
        return models;
    }


    public Tarifa save(Tarifa tarifa) {
        Tarifa saved = this.tarifaRepository.save(tarifa);
        return saved;
    }

    public Tarifa update(Tarifa tarifa) {
        Tarifa updated = this.tarifaRepository.save(tarifa);
        return updated;
    }

    public void delete(Long tarifaId){
        this.tarifaRepository.deleteById(tarifaId);
        return;
    }

}
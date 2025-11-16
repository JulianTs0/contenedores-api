package backend.grupo130.envios.repository;

import backend.grupo130.envios.data.models.SeguimientoEnvio;
import backend.grupo130.envios.data.repository.PostgresSeguimientoEnvioRepositoryI;
import backend.grupo130.envios.data.repository.PostgresSolicitudRepositoryI;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@AllArgsConstructor
public class SeguimientoEnvioRepository {

    private final PostgresSeguimientoEnvioRepositoryI seguimientoRepository;

    public SeguimientoEnvio getById(Long seguimientoId){
        SeguimientoEnvio model = this.seguimientoRepository.findById(seguimientoId).orElse(null);
        return model;
    }

    public List<SeguimientoEnvio> getAll() {
        List<SeguimientoEnvio> models = this.seguimientoRepository.findAll();
        return models;
    }


    public SeguimientoEnvio save(SeguimientoEnvio seguimientoEnvio) {
        SeguimientoEnvio saved = this.seguimientoRepository.save(seguimientoEnvio);
        return saved;
    }

    public SeguimientoEnvio update(SeguimientoEnvio seguimientoEnvio) {
        SeguimientoEnvio updated = this.seguimientoRepository.save(seguimientoEnvio);
        return updated;
    }

    public void delete(Long seguimientoId){
        this.seguimientoRepository.deleteById(seguimientoId);
        return;
    }

}
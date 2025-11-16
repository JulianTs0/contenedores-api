package backend.grupo130.envios.repository;

import backend.grupo130.envios.data.models.SolicitudTraslado;
import backend.grupo130.envios.data.repository.PostgresSolicitudRepositoryI;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@AllArgsConstructor
public class SolicitudTrasladoRepository {

    private final PostgresSolicitudRepositoryI solicitudRepository;

    public SolicitudTraslado getById(Long solicitudId){
        SolicitudTraslado model = this.solicitudRepository.findById(solicitudId).orElse(null);
        return model;
    }

    public List<SolicitudTraslado> getAll() {
        List<SolicitudTraslado> models = this.solicitudRepository.findAll();
        return models;
    }


    public SolicitudTraslado save(SolicitudTraslado solicitud) {
        SolicitudTraslado saved = this.solicitudRepository.save(solicitud);
        return saved;
    }

    public SolicitudTraslado update(SolicitudTraslado solicitud) {
        SolicitudTraslado updated = this.solicitudRepository.save(solicitud);
        return updated;
    }

    public void delete(Long solicitudId){
        this.solicitudRepository.deleteById(solicitudId);
        return;
    }

}

package backend.grupo130.envios.repository;

import backend.grupo130.envios.data.PersistenceMapper;
import backend.grupo130.envios.data.entity.SolicitudTraslado;
import backend.grupo130.envios.data.models.SolicitudTrasladoModel;
import backend.grupo130.envios.data.repository.PostgresSolicitudRepositoryI;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@AllArgsConstructor
public class SolicitudTrasladoRepository {

    private final PostgresSolicitudRepositoryI solicitudRepository;
    private final PersistenceMapper persistenceMapper;

    public SolicitudTraslado getById(Long solicitudId){
        SolicitudTrasladoModel model = this.solicitudRepository.findById(solicitudId).orElse(null);
        return PersistenceMapper.toDomain(model);
    }

    public List<SolicitudTraslado> getAll() {
        List<SolicitudTrasladoModel> models = this.solicitudRepository.findAll();
        return PersistenceMapper.toSolicitudTrasladoDomainList(models);
    }


    public SolicitudTraslado save(SolicitudTraslado solicitud) {
        SolicitudTrasladoModel model = PersistenceMapper.toModel(solicitud);
        SolicitudTrasladoModel saved = this.solicitudRepository.save(model);
        return PersistenceMapper.toDomain(saved);
    }

    public SolicitudTraslado update(SolicitudTraslado solicitud) {
        SolicitudTrasladoModel model = PersistenceMapper.toModel(solicitud);
        SolicitudTrasladoModel updated = this.solicitudRepository.save(model);
        return PersistenceMapper.toDomain(updated);
    }

    public void delete(Long solicitudId){
        this.solicitudRepository.deleteById(solicitudId);
    }

}

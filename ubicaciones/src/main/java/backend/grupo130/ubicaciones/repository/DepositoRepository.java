
package backend.grupo130.ubicaciones.repository;

import backend.grupo130.ubicaciones.data.PersistenceMapper;
import backend.grupo130.ubicaciones.data.entity.Deposito;
import backend.grupo130.ubicaciones.data.models.DepositoModel;
import backend.grupo130.ubicaciones.data.repository.PostgresDepositoRepositoryI;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@AllArgsConstructor
public class DepositoRepository {

    private final PostgresDepositoRepositoryI depositoRepositoryI;

    public Deposito getById(Long idDeposito){
        DepositoModel model = this.depositoRepositoryI.findById(idDeposito).orElse(null);
        return PersistenceMapper.toDomain(model);
    }

    public List<Deposito> getAll() {
        List<DepositoModel> models = this.depositoRepositoryI.findAll();
        return PersistenceMapper.toDomainDeposito(models);
    }

    public Deposito save(Deposito deposito) {
        DepositoModel model = PersistenceMapper.toModel(deposito);
        DepositoModel saved = this.depositoRepositoryI.save(model);
        return PersistenceMapper.toDomain(saved);
    }

    public Deposito update(Deposito deposito) {
        DepositoModel model = PersistenceMapper.toModel(deposito);
        DepositoModel updated = this.depositoRepositoryI.save(model);
        return PersistenceMapper.toDomain(updated);
    }

    public void delete(Long idDeposito){
        this.depositoRepositoryI.deleteById(idDeposito);
    }

}


package backend.grupo130.envios.data;

import backend.grupo130.envios.data.entity.PreciosNegocio;
import backend.grupo130.envios.data.entity.SeguimientoEnvio;
import backend.grupo130.envios.data.entity.SolicitudTraslado;
import backend.grupo130.envios.data.entity.Tarifa;
import backend.grupo130.envios.data.models.PreciosNegocioModel;
import backend.grupo130.envios.data.models.SeguimientoEnvioModel;
import backend.grupo130.envios.data.models.SolicitudTrasladoModel;
import backend.grupo130.envios.data.models.TarifaModel;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PersistenceMapper {

    public static SolicitudTraslado toDomain(SolicitudTrasladoModel model) {
        if (model == null) {
            return null;
        }

        return new SolicitudTraslado(
                model.getIdSolicitud(),
                model.getFechaInicio(),
                model.getFechaFin(),
                model.getTiempoEstimadoHoras(),
                model.getTiempoRealHoras(),
                PersistenceMapper.toDomain(model.getTarifa()),
                model.getEstado(),
                PersistenceMapper.toSeguimientoEnvioDomainList(model.getSeguimientos()),
                model.getIdContenedor(),
                model.getIdCliente(),
                model.getIdOrigen(),
                model.getIdDestino()
        );
    }

    public static SolicitudTrasladoModel toModel(SolicitudTraslado domain) {
        if (domain == null) {
            return null;
        }
        
        SolicitudTrasladoModel model = new SolicitudTrasladoModel();
        model.setIdSolicitud(domain.getIdSolicitud());
        model.setFechaInicio(domain.getFechaInicio());
        model.setFechaFin(domain.getFechaFin());
        model.setTiempoEstimadoHoras(domain.getTiempoEstimadoHoras());
        model.setTiempoRealHoras(domain.getTiempoRealHoras());
        model.setTarifa(PersistenceMapper.toModel(domain.getTarifa()));
        model.setEstado(domain.getEstado());
        model.setSeguimientos(PersistenceMapper.toSeguimientoEnvioModelList(domain.getSeguimientos()));
        model.setIdContenedor(domain.getIdContenedor());
        model.setIdCliente(domain.getIdCliente());
        model.setIdOrigen(domain.getIdOrigen());
        model.setIdDestino(domain.getIdDestino());
        
        return model;
    }

    public static List<SolicitudTraslado> toSolicitudTrasladoDomainList(List<SolicitudTrasladoModel> models) {
        return models.stream()
                .map(PersistenceMapper::toDomain)
                .collect(Collectors.toList());
    }

    public static List<SolicitudTrasladoModel> toSolicitudTrasladoModelList(List<SolicitudTraslado> domains) {
        return domains.stream()
                .map(PersistenceMapper::toModel)
                .collect(Collectors.toList());
    }

    public static SeguimientoEnvio toDomain(SeguimientoEnvioModel model) {
        if (model == null) {
            return null;
        }
        return new SeguimientoEnvio(
                model.getIdSeguimiento(),
                model.getFechaHoraInicio(),
                model.getFechaHoraFin(),
                model.getEstado(),
                model.getDescripcion()
        );
    }

    public static SeguimientoEnvioModel toModel(SeguimientoEnvio domain) {
        if (domain == null) {
            return null;
        }
        SeguimientoEnvioModel model = new SeguimientoEnvioModel();
        model.setIdSeguimiento(domain.getIdSeguimiento());
        model.setFechaHoraInicio(domain.getFechaHoraInicio());
        model.setFechaHoraFin(domain.getFechaHoraFin());
        model.setEstado(domain.getEstado());
        model.setDescripcion(domain.getDescripcion());
        return model;
    }

    public static List<SeguimientoEnvio> toSeguimientoEnvioDomainList(List<SeguimientoEnvioModel> models) {
        if (models == null) {
            return null;
        }
        return models.stream()
                .map(PersistenceMapper::toDomain)
                .collect(Collectors.toList());
    }

    public static List<SeguimientoEnvioModel> toSeguimientoEnvioModelList(List<SeguimientoEnvio> domains) {
        if (domains == null) {
            return null;
        }
        return domains.stream()
                .map(PersistenceMapper::toModel)
                .collect(Collectors.toList());
    }
    
    public static Tarifa toDomain(TarifaModel model) {
        if (model == null) {
            return null;
        }
        Tarifa entity = new Tarifa();
        entity.setIdTarifa(model.getIdTarifa());
        entity.setPesoMax(model.getPesoMax());
        entity.setVolumenMax(model.getVolumenMax());
        entity.setCostoBase(model.getCostoBase());
        entity.setValorLitro(model.getValorLitro());
        entity.setConsumoAprox(model.getConsumoAprox());
        entity.setCostoEstadia(model.getCostoEstadia());
        entity.setCostoEstimado(model.getCostoEstimado());
        entity.setCostoFinal(model.getCostoFinal());
        return entity;
    }

    public static TarifaModel toModel(Tarifa domain) {
        if (domain == null) {
            return null;
        }
        TarifaModel model = new TarifaModel();
        model.setIdTarifa(domain.getIdTarifa());
        model.setPesoMax(domain.getPesoMax());
        model.setVolumenMax(domain.getVolumenMax());
        model.setCostoBase(domain.getCostoBase());
        model.setValorLitro(domain.getValorLitro());
        model.setConsumoAprox(domain.getConsumoAprox());
        model.setCostoEstadia(domain.getCostoEstadia());
        model.setCostoEstimado(domain.getCostoEstimado());
        model.setCostoFinal(domain.getCostoFinal());
        return model;
    }

    public static PreciosNegocio toDomain(PreciosNegocioModel model) {
        if (model == null) {
            return null;
        }
        return new PreciosNegocio(
                model.getIdPreciosNegocio(),
                model.getRangoPesoBajo(),
                model.getRangoPesoMedio(),
                model.getMultiplicadorBajo(),
                model.getMultiplicadorMedio(),
                model.getMultiplicadorAlto(),
                model.getValorLitro(),
                model.getCargoGestion(),
                model.getFechaCreacion(),
                model.getFechaActualizacion()
        );
    }

    public static PreciosNegocioModel toModel(PreciosNegocio domain) {
        if (domain == null) {
            return null;
        }
        PreciosNegocioModel model = new PreciosNegocioModel();
        model.setIdPreciosNegocio(domain.getIdPreciosNegocio());
        model.setRangoPesoBajo(domain.getRangoPesoBajo());
        model.setRangoPesoMedio(domain.getRangoPesoMedio());
        model.setMultiplicadorBajo(domain.getMultiplicadorBajo());
        model.setMultiplicadorMedio(domain.getMultiplicadorMedio());
        model.setMultiplicadorAlto(domain.getMultiplicadorAlto());
        model.setValorLitro(domain.getValorLitro());
        model.setCargoGestion(domain.getCargoGestion());
        model.setFechaCreacion(domain.getFechaCreacion());
        model.setFechaActualizacion(domain.getFechaActualizacion());
        return model;
    }

    public static List<PreciosNegocio> toPreciosNegocioDomainList(List<PreciosNegocioModel> models) {
        if (models == null) {
            return null;
        }
        return models.stream()
                .map(PersistenceMapper::toDomain)
                .collect(Collectors.toList());
    }
}

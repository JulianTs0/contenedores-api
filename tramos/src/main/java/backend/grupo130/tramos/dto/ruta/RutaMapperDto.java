package backend.grupo130.tramos.dto.ruta;

import backend.grupo130.tramos.client.envios.entity.SolicitudTraslado;
import backend.grupo130.tramos.client.envios.entity.Tarifa;
import backend.grupo130.tramos.data.entity.RutaTraslado;
import backend.grupo130.tramos.data.entity.Tramo;
import backend.grupo130.tramos.dto.ruta.response.RutaGetAllResponse;
import backend.grupo130.tramos.dto.ruta.response.RutaGetByIdResponse;
import backend.grupo130.tramos.dto.ruta.response.RutaGetOpcionesResponse;

import java.math.BigDecimal;
import java.util.List;

public class RutaMapperDto {

    public static RutaGetByIdResponse toResponseGetById(RutaTraslado ruta, SolicitudTraslado solicitud) {
        return new RutaGetByIdResponse(
            ruta.getIdRuta(),
            ruta.getCantidadTramos(),
            ruta.getCantidadDepositos(),
            ruta.getCargosGestionFijo(),
            solicitud
        );
    }

    public static RutaGetAllResponse toResponseGetAll(List<RutaTraslado> rutas) {
        return new RutaGetAllResponse(
            rutas
        );
    }

    public static RutaGetOpcionesResponse toResponseGetOpciones(Tarifa tarifaAprox, BigDecimal tiempoEstimado, RutaTraslado ruta, List<Tramo> tramos){
        return new RutaGetOpcionesResponse(
            tarifaAprox,
            tiempoEstimado,
            ruta,
            tramos
        );
    }

}

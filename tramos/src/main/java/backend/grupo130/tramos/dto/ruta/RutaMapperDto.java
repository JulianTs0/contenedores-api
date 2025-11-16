package backend.grupo130.tramos.dto.ruta;

import backend.grupo130.tramos.client.envios.models.SolicitudTraslado;
import backend.grupo130.tramos.client.envios.models.Tarifa;
import backend.grupo130.tramos.data.models.RutaTraslado;
import backend.grupo130.tramos.data.models.Tramo;
import backend.grupo130.tramos.dto.ruta.response.RutaGetAllResponse;
import backend.grupo130.tramos.dto.ruta.response.RutaGetByIdResponse;
import backend.grupo130.tramos.dto.ruta.response.RutaGetOpcionesResponse;

import java.math.BigDecimal;
import java.util.List;

public class RutaMapperDto {

    public static RutaGetByIdResponse toResponseGet(RutaTraslado ruta, SolicitudTraslado solicitud) {
        return new RutaGetByIdResponse(
            ruta.getIdRuta(),
            ruta.getCantidadTramos(),
            ruta.getCantidadDepositos(),
            ruta.getCargosGestionFijo(),
            solicitud
        );
    }

    public static RutaGetAllResponse toResponseGet(List<RutaTraslado> rutas) {
        return new RutaGetAllResponse(
            rutas
        );
    }

    public static RutaGetOpcionesResponse toResponseGet(Tarifa tarifaAprox, BigDecimal tiempoEstimado, RutaTraslado ruta, List<Tramo> tramos){
        return new RutaGetOpcionesResponse(
            tarifaAprox,
            tiempoEstimado,
            ruta,
            tramos
        );
    }

}

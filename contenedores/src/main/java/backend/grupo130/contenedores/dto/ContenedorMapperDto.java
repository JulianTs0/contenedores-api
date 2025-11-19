package backend.grupo130.contenedores.dto;

import backend.grupo130.contenedores.client.usuarios.entity.Usuario;
import backend.grupo130.contenedores.config.enums.EstadoContenedor;
import backend.grupo130.contenedores.data.models.Contenedor;
import backend.grupo130.contenedores.dto.response.*;

import java.util.List;

public class ContenedorMapperDto {

    public static GetByIdResponse toResponseGet(Contenedor contenedor, Usuario usuario) {
        return new GetByIdResponse(
            contenedor.getIdContenedor(),
            contenedor.getPeso(),
            contenedor.getVolumen(),
            usuario,
            contenedor.getEstado().name()
        );
    }

    public static GetByPesoVolumenResponse toResponseGet(Contenedor contenedor) {
        return new GetByPesoVolumenResponse(
            contenedor.getIdContenedor()
        );
    }

    public static GetAllResponse toResponseGet(List<Contenedor> contenedores) {
        return new GetAllResponse(
            contenedores
        );
    }

    public static RegisterResponse toResponsePost(Contenedor contenedor) {
        return new RegisterResponse(
            contenedor.getIdContenedor()
        );
    }

    public static EditResponse toResponsePatch(Contenedor contenedor) {
        return new EditResponse(
            contenedor.getIdContenedor(),
            contenedor.getPeso(),
            contenedor.getPeso(),
            contenedor.getIdCliente(),
            contenedor.getEstado().name()
        );
    }

    public static CambioDeEstadoResponse toResponsePatch(Long id, EstadoContenedor estadoContenedor) {
        return new CambioDeEstadoResponse(
            id,
            estadoContenedor.toString()
        );
    }

}

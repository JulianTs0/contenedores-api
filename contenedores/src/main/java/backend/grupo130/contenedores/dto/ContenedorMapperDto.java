package backend.grupo130.contenedores.dto;

import backend.grupo130.contenedores.client.usuarios.entity.Usuario;
import backend.grupo130.contenedores.config.enums.EstadoContenedor;
import backend.grupo130.contenedores.data.entity.Contenedor;
import backend.grupo130.contenedores.dto.request.AsignarClienteRequest;
import backend.grupo130.contenedores.dto.request.CambioDeEstadoRequest;
import backend.grupo130.contenedores.dto.request.EditRequest;
import backend.grupo130.contenedores.dto.response.*;

import java.util.List;
import java.util.stream.Collectors;

public class ContenedorMapperDto {

    public static GetByIdResponse toResponseGetById(Contenedor contenedor) {
        return new GetByIdResponse(
            contenedor.getIdContenedor(),
            contenedor.getPeso(),
            contenedor.getVolumen(),
            contenedor.getCliente(),
            contenedor.getEstado().name()
        );
    }

    public static GetByPesoVolumenResponse toResponseGetByPesoVolumen(Contenedor contenedor) {
        return new GetByPesoVolumenResponse(
            contenedor.getIdContenedor(),
            contenedor.getPeso(),
            contenedor.getVolumen(),
            contenedor.getCliente(),
            contenedor.getEstado().name()
        );
    }

    public static GetAllResponse toResponseGetAll(List<Contenedor> contenedores) {
        return new GetAllResponse(
            contenedores.stream().map(ContenedorMapperDto::toResponseGetById).collect(Collectors.toList())
        );
    }

    public static RegisterResponse toResponsePostRegister(Contenedor contenedor) {
        return new RegisterResponse(
            contenedor.getIdContenedor()
        );
    }

    public static EditRequest toRequestPatchEdit(Long id, EditRequest body) {
        return EditRequest.builder()
            .id(id)
            .peso(body.getPeso())
            .volumen(body.getVolumen())
            .build();
    }

    public static EditResponse toResponsePatchEdit(Contenedor contenedor) {
        return new EditResponse(
            contenedor.getIdContenedor(),
            contenedor.getPeso(),
            contenedor.getVolumen(),
            contenedor.getCliente() != null ? contenedor.getCliente().getIdUsuario() : null,
            contenedor.getEstado().name()
        );
    }

    public static CambioDeEstadoRequest toRequestPatchEstado(Long id, CambioDeEstadoRequest body) {
        return CambioDeEstadoRequest.builder()
            .id(id)
            .estado(body.getEstado())
            .build();
    }

    public static CambioDeEstadoResponse toResponsePatchEstado(Contenedor contenedor) {
        return new CambioDeEstadoResponse(
            contenedor.getIdContenedor(),
            contenedor.getPeso(),
            contenedor.getVolumen(),
            contenedor.getCliente(),
            contenedor.getEstado().name()
        );
    }

    public static AsignarClienteRequest toRequestPatchCliente(Long id, AsignarClienteRequest body) {
        return AsignarClienteRequest.builder()
            .id(id)
            .idCliente(body.getIdCliente())
            .build();
    }

    public static AsignarClienteResponse toResponsePatchCliente(Contenedor contenedor, Usuario usuario) {
        return new AsignarClienteResponse(
            contenedor.getIdContenedor(),
            contenedor.getPeso(),
            contenedor.getVolumen(),
            usuario,
            contenedor.getEstado()
        );
    }
}

package backend.grupo130.usuarios.dto;

import backend.grupo130.usuarios.data.models.Usuario;
import backend.grupo130.usuarios.dto.response.EditResponse;
import backend.grupo130.usuarios.dto.response.GetAllResponse;
import backend.grupo130.usuarios.dto.response.GetByIdResponse;

import java.util.List;

public class UsuarioMapperDto {

    public static GetByIdResponse toResponseGet(Usuario usuario) {
        return new GetByIdResponse(
            usuario.getIdUsuario(),
            usuario.getNombre(),
            usuario.getApellido(),
            usuario.getTelefono(),
            usuario.getEmail(),
            usuario.getRol().name()
        );
    }

    public static GetAllResponse toResponseGet(List<Usuario> usuarios) {
        return new GetAllResponse(
            usuarios.stream().map(UsuarioMapperDto::toResponseGet).toList()
        );
    }

    public static EditResponse toResponsePatch(Usuario usuario) {
        return new EditResponse(
            usuario.getIdUsuario(),
            usuario.getNombre(),
            usuario.getApellido(),
            usuario.getTelefono(),
            usuario.getEmail()
        );
    }

}

package backend.grupo130.usuarios.dto;

import backend.grupo130.usuarios.data.entity.Usuario;
import backend.grupo130.usuarios.dto.request.EditRequest;
import backend.grupo130.usuarios.dto.response.EditResponse;
import backend.grupo130.usuarios.dto.response.GetAllResponse;
import backend.grupo130.usuarios.dto.response.GetByIdResponse;
import backend.grupo130.usuarios.dto.response.RegisterResponse;

import java.util.List;

public class UsuarioMapperDto {

    public static GetByIdResponse toResponseGetId(Usuario usuario) {
        return new GetByIdResponse(
            usuario.getIdUsuario(),
            usuario.getNombre(),
            usuario.getApellido(),
            usuario.getTelefono(),
            usuario.getEmail(),
            usuario.getRol().name()
        );
    }

    public static GetAllResponse toResponseGetAll(List<Usuario> usuarios) {
        return new GetAllResponse(
            usuarios.stream().map(UsuarioMapperDto::toResponseGetId).toList()
        );
    }

    public static EditRequest toRequestPatchEdit(Long id, EditRequest body) {
        return EditRequest.builder()
            .id(id)
            .nombre(body.getNombre())
            .apellido(body.getApellido())
            .email(body.getEmail())
            .telefono(body.getTelefono())
            .build();
    }

    public static EditResponse toResponsePatchEdit(Usuario usuario) {
        return new EditResponse(
            usuario.getIdUsuario(),
            usuario.getNombre(),
            usuario.getApellido(),
            usuario.getTelefono(),
            usuario.getEmail()
        );
    }

    public static RegisterResponse toResponsePostRegister(Usuario usuario) {
        return new RegisterResponse(
            usuario.getIdUsuario()
        );
    }

}

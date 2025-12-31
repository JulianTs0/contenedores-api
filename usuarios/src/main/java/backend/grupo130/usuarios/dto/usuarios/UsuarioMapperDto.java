package backend.grupo130.usuarios.dto.usuarios;

import backend.grupo130.usuarios.data.entity.Usuario;
import backend.grupo130.usuarios.dto.usuarios.request.EditRequest;
import backend.grupo130.usuarios.dto.usuarios.response.EditResponse;
import backend.grupo130.usuarios.dto.usuarios.response.GetAllResponse;
import backend.grupo130.usuarios.dto.usuarios.response.GetByIdResponse;
import backend.grupo130.usuarios.dto.usuarios.response.RegisterResponse;

import java.util.List;
import java.util.stream.Collectors;

public class UsuarioMapperDto {

    public static GetByIdResponse toResponseGetId(Usuario usuario) {
        return new GetByIdResponse(
            usuario.getIdUsuario(),
            usuario.getNombre(),
            usuario.getApellido(),
            usuario.getTelefono(),
            usuario.getEmail(),
            usuario.getRoles().stream().map(Enum::name).collect(Collectors.toSet())
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

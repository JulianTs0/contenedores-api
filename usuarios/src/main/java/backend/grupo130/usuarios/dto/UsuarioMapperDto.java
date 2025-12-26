package backend.grupo130.usuarios.dto;

import backend.grupo130.usuarios.data.entity.Usuario;
import backend.grupo130.usuarios.dto.request.EditRequest;
import backend.grupo130.usuarios.dto.response.EditResponse;
import backend.grupo130.usuarios.dto.response.GetAllResponse;
import backend.grupo130.usuarios.dto.response.GetByIdResponse;
import backend.grupo130.usuarios.dto.response.RegisterResponse;

import java.util.List;
import java.util.Map;

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

    public static EditRequest toRequestPatchEdit(Long id, Map<String, Object> body) {
        return new EditRequest(
            id,
            (String) body.get("nombre"),
            (String) body.get("apellido"),
            (String) body.get("telefono"),
            (String) body.get("email")
        );
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

package backend.grupo130.usuarios.data;

import backend.grupo130.usuarios.data.entity.Usuario;
import backend.grupo130.usuarios.data.models.UsuarioModel;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class PersistanceMapper {

    public static Usuario toDomain(UsuarioModel usuarioModel){
        if (usuarioModel == null) return null;

        return new Usuario(
            usuarioModel.getIdUsuario(),
            usuarioModel.getKeycloakId(),
            usuarioModel.getNombre(),
            usuarioModel.getApellido(),
            usuarioModel.getTelefono(),
            usuarioModel.getEmail(),
            usuarioModel.getRoles()
        );
    }

    public static UsuarioModel toModel(Usuario usuario){
        if (usuario == null) return null;

        return new UsuarioModel(
            usuario.getIdUsuario(),
            usuario.getKeycloakId(),
            usuario.getNombre(),
            usuario.getApellido(),
            usuario.getTelefono(),
            usuario.getEmail(),
            usuario.getRoles()
        );
    }

    public static List<Usuario> toDomain(List<UsuarioModel> userModels) {
        if (userModels == null) return Collections.emptyList();

        return userModels.stream()
            .map(PersistanceMapper::toDomain)
            .collect(Collectors.toList());
    }

    public static List<UsuarioModel> toModel(List<Usuario> users) {
        if (users == null) return Collections.emptyList();

        return users.stream()
            .map(PersistanceMapper::toModel)
            .collect(Collectors.toList());
    }

}

package backend.grupo130.tramos.config.enums;

import lombok.Getter;

@Getter
public enum Descripcciones {

    CREADA("Solicitud creada."),
    CONFIRMADA("La solicitud a sido confirmada"),
    PROGRAMADA("Todos los tramos fueron asignados"),
    INICIADA("El primer tramo inicio"),
    FINALIZADA("Todos los tramos fueron finalizados");

    private final String descripccion;

    Descripcciones(String descripccion){
        this.descripccion = descripccion;
    }

}

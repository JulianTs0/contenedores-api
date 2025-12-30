package backend.grupo130.envios.config.enums;

import lombok.Getter;
import org.aspectj.weaver.NewConstructorTypeMunger;

@Getter
public enum Descripcciones {

    CREADA("Solicitud creada."),
    CONFIRMADA("La solicitud a sido confirmada");

    private final String descripccion;

    Descripcciones(String descripccion){
        this.descripccion = descripccion;
    }

}

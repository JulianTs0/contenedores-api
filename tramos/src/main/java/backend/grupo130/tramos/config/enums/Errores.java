package backend.grupo130.tramos.config.enums;

import lombok.Getter;

@Getter
public enum Errores {

    ERROR_INTERNO("Error interno"),
    ACCION_NO_AUTORIZADA("No esta autorizado a registrar esta accion"),

    RUTA_NO_ENCONTRADA("Ruta no encontrada"),
    RUTA_SIN_TRAMOS("La ruta no se le han asignado los tramos todavia"),
    RUTA_SIN_CONDUCTORES("La ruta todavia no tiene conductores designados"),

    TRAMO_NO_ENCONTRADO("Tramo no encontrado"),
    TRAMOS_UBICACION_INVALIDA("Los tramos son invalidos debido a su ubicacion"),
    TRAMO_YA_ASIGNADO("El tramo ya fue asignado"),
    TRAMO_NO_ASIGNADO("No se puede registar el inicio de un tramo que no esta asignado"),
    TRAMO_ANTERIOR_NO_FINALIZADO("No se puede iniciar este tramo. El tramo anterior aún no ha sido terminado."),
    TRAMO_NO_INICIADO("No se puede registar el fin de un tramo que no esta iniciado"),

    SOLICITUD_NO_ENCONTRADA("Solicitud no encontrada"),
    SOLICITUD_NO_PROGRAMADA("No se puede registar el inicio de un tramo de una solicitud que no esta programada"),
    SOLICITUD_NO_INICIADA("No se puede registar el fin de un tramo de una solicitud que no esta iniciada"),
    CAMION_NO_ENCONTRADO("Camion no encontrado"),
    CAMION_NO_DISPONIBLE("El camion no esta disponible"),
    CAMIONES_NO_ENCONTRADOS("No se encontraros camiones disponibles"),
    TARIFA_NO_ENCONTRADA("La Solicitud no tiene Tarifa asociada. No se puede calcular el costo final");

    private final String mensaje;

    Errores(String mensaje) {
        this.mensaje = mensaje;
    }


}

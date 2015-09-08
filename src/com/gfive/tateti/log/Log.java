package com.gfive.tateti.log;

/**
 * Permite guardar y mostrar mensajes del funcionamiento de la aplicación.
 * 
 * @author nicolas
 *
 */
public class Log {

    /**
     * Imprime un mensaje orientado a la solución de posible errores de programación.
     * 
     * @param mensaje
     */
    public void debug(Object mensaje) {
        System.out.println("[DEBUG]" + mensaje.toString());
    }

    /**
     * Imprime un mensaje que indica un error.
     * 
     * @param mensaje
     */
    public void error(Object mensaje) {
        System.err.println("[ERROR]" + mensaje);
    }

    /**
     * Reporta una excepción como error.
     * 
     * @param causa
     */
    public void reportarError(Throwable causa) {
        error(causa.getMessage());
        causa.printStackTrace(System.err);
    }
}

package com.gfive.tateti.metricas;

import java.util.List;

/**
 * Cuenta la cantidad de líneas en un archivo de código fuente.
 * @author nicolas
 *
 */
public class CantidadLineas implements Metrica {
    
    /**
     * Nombre de la métrica.
     */
    private static final String NOMBRE_CANTIDAD_LINEAS = "Cantidad de líneas";
    
    /**
     * La cantidad de líneas que tiene el archivo.
     */
    private Integer cantidadLineas;

    @Override
    public void procesar(List<String> lineasArchivo) {
        if (cantidadLineas != null)
            throw new RuntimeException("La cantidad de líneas se calculó antes");
        cantidadLineas = lineasArchivo.size();
    }

    @Override
    public String getNombre() {
        return NOMBRE_CANTIDAD_LINEAS;
    }

    @Override
    public String getValor() {
        if (cantidadLineas == null)
            throw new RuntimeException("La cantidad de líneas todavía no se calculó");

        return Integer.toString(cantidadLineas);
    }

}

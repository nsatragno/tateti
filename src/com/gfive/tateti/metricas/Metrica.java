package com.gfive.tateti.metricas;

import java.util.List;

/**
 * Métrica que se aplica a un archivo de código fuente.
 * Antes de intentar mostrar una métrica, debe procesarse.
 * Las métricas pueden ser procesadas una sola vez en su vida.
 * @author nicolas
 *
 */
public interface Metrica {

    /**
     * Realiza el procesamiento del archivo, llenando los valores de la métrica.
     * @param lineasArchivo - un listado con todas las líneas del archivo.
     * @throws RuntimeException si la métrica intenta procesarse dos veces.
     */
    public void procesar(List<String> lineasArchivo) throws RuntimeException;
    
    /**
     * Devuelve una nueva métrica que es el resultado de agregar esta métrica a la métrica pasada
     * por parámetro.
     * 
     * @param metrica - la métrica con la que esta debe agregarse.
     */
    public Metrica agregar(Metrica metrica);
    
    /**
     * @return el nombre amigable para el usuario de la métrica.
     */
    public String getNombre();
    
    /**
     * @return el valor de la métrica, formateado para que sea amigable para el usuario.
     * @throws RuntimeException si se intenta obtener el valor de una métrica cuyo procesamiento no
     * ha concluido.
     */
    public String getValor() throws RuntimeException;
}

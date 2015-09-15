package com.gfive.tateti.metricas;

import java.util.List;

import com.gfive.tateti.log.Log;

/**
 * Cuenta la cantidad de líneas con comentarios.
 * @author nicolas
 *
 */
public class CantidadComentarios implements Metrica {
    
    /**
     * Nombre de la métrica.
     */
    private static final String NOMBRE_METRICAS = "Cantidad de líneas con comentarios";
    
    /**
     * La cantidad de líneas con comentarios que tiene el archivo.
     */
    private Integer cantidadLineas;
    
    /**
     * Constructor por defecto.
     */
    public CantidadComentarios() { }

    /**
     * Construye una métrica de cantidad de líneas de comentarios a partir de una cantidad dada.
     * @param cantidadLineas
     */
    private CantidadComentarios(int cantidadLineas) {
        this.cantidadLineas = cantidadLineas;
    }

    @Override
    public void procesar(List<String> lineasArchivo) {
        Log log = new Log();
        cantidadLineas = 0;
        // Indica si estamos en un comentario /* */
        boolean enComentarioMultilinea = false;
        for (String linea : lineasArchivo) {
            log.debug("Leyendo línea " + linea);

            // Indica si la línea ya se ha contado como de comentario.
            boolean lineaContada = false;

            // Indica si estamos dentro de un par de comillas.
            boolean enComillas = false;
            for (int i = 0; i < linea.length() - 1; i++) {
                // Buscamos comillas.
                if (tieneComillas(linea, i)) {
                    log.debug("Encontrada comilla no escapada");
                    enComillas = !enComillas;
                    log.debug("En comillas: " + enComillas);
                }

                if (enComillas)
                    continue;

                if (!lineaContada &&
                    !enComillas &&
                    tieneComentario(linea, i)) {
                    log.debug("Encontrado comentario //");
                    cantidadLineas++;
                    lineaContada = true;
                    continue;
                }

                if (!enComentarioMultilinea &&
                    arrancaComentarioMultilinea(linea, i)) {
                    log.debug("Encontrado comentario multilínea");
                    enComentarioMultilinea = true;
                }
                if (enComentarioMultilinea && !lineaContada) {
                    log.debug("Esta línea tiene un comentario. Se cuenta");
                    lineaContada = true;
                    cantidadLineas++;
                }
                if (terminaComentarioMultilinea(linea, i)) {
                    log.debug("Se termina el comentario multilínea");
                    enComentarioMultilinea = false;
                }
            }
        }
    }
    
    private boolean tieneComentario(String linea, int indice) {
        return linea.charAt(indice) == '/' && linea.charAt(indice + 1) == '/';
    }

    private boolean tieneComillas(String linea, int indice) {
        return linea.charAt(indice) != '\\' && linea.charAt(indice + 1) == '"';
    }

    private boolean arrancaComentarioMultilinea(String linea, int indice) {
        return linea.charAt(indice) == '/' && linea.charAt(indice + 1) == '*';
    }

    private boolean terminaComentarioMultilinea(String linea, int indice) {
        return linea.charAt(indice) == '*' && linea.charAt(indice + 1) == '/';
    }

    @Override
    public String getNombre() {
        return NOMBRE_METRICAS;
    }

    @Override
    public String getValor() {
        if (cantidadLineas == null)
            throw new RuntimeException("La cantidad de líneas todavía no se calculó");

        return Integer.toString(cantidadLineas);
    }

    @Override
    public Metrica agregar(Metrica metrica) {
        CantidadComentarios otra = (CantidadComentarios)metrica;
        return new CantidadComentarios(cantidadLineas + otra.cantidadLineas);
    }

}

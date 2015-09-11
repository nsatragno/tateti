package com.gfive.tateti.componentes.arbol;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import com.gfive.tateti.log.Log;
import com.gfive.tateti.metricas.Metrica;
import com.gfive.tateti.metricas.MetricasFactory;

/**
 * Dato para los nodos del árbol de archivos.
 * @author nicolas
 *
 */
public class ArchivoNodo extends NodoArbol {
    
    /**
     * ID de serie por defecto.
     */
    private static final long serialVersionUID = 1L;


    /**
     * Construye un ArchivoNodo por path.
     * @param path - la ruta al archivo.
     */
    public ArchivoNodo(Path path) {
        super(path);
    }
    
    
    @Override
    public List<Metrica> calcularMetricas() {
        // Cargo el archivo completo en memoria.
        final List<String> lineasArchivo;
        try {
            lineasArchivo =  Files.readAllLines(getRutaArchivo(), StandardCharsets.ISO_8859_1);
        } catch (IOException e) {
            Log log = new Log();
            log.error("Error al intentar calcular las métricas de " + getRutaArchivo());
            log.reportarError(e);
            throw new RuntimeException(e);
        }
        
        return MetricasFactory.get()
            .getMetricas()
            .peek(metrica -> metrica.procesar(lineasArchivo))
            .collect(Collectors.toList());
    }
    

    @Override
    public boolean esCarpeta() {
        return false;
    }
}

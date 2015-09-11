package com.gfive.tateti.componentes.arbol;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

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
    protected List<Metrica> calcularMetricas() {
        // Cargo el archivo completo en memoria.
        final List<String> lineasArchivo;
        try {
            lineasArchivo =  Files.lines(getRutaArchivo()).collect(Collectors.toList());
        } catch (IOException e) {
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

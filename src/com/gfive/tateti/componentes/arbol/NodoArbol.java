package com.gfive.tateti.componentes.arbol;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

import javax.swing.tree.DefaultMutableTreeNode;

import com.gfive.tateti.metricas.Metrica;

/**
 * Nodo del árbol de directorios. Puede ser un archivo o una carpeta.
 * @author nicolas
 *
 */
public abstract class NodoArbol extends DefaultMutableTreeNode {
    
    /**
     * ID de serie por defecto.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Path asociado al NodoArbol.
     */
    private final Path path;

    /**
     * Resultados del procesamiento de métricas.
     */
    private final List<Metrica> metricas;

    /**
     * Construye una instancia específica de NodoArbol de acuerdo al path dado.
     * @param path
     * @return
     */
    public static NodoArbol construir(Path path) {
        if (Files.isDirectory(path))
            return new NodoCarpeta(path);
        return new ArchivoNodo(path);
    }

    /**
     * Construye un NodoArbol por path.
     * @param path - la ruta al archivo.
     */
    protected NodoArbol(Path path) {
        Objects.requireNonNull(path);
        this.path = path;
        metricas = calcularMetricas();
    }
    
    /**
     * @return la ruta del archivo.
     */
    public Path getRutaArchivo() {
        return path;
    }

    /**
     * @return las métricas del nodo.
     */
    public List<Metrica> getMetricas() {
        return metricas;
    }

    @Override
    public String toString() {
        return path.getFileName().toString();
    }
    
    /**
     * @return true si el nodo es un directorio, false si es un archivo.
     */
    public abstract boolean esCarpeta();
    
    /**
     * @return el listado de métricas completo y calculado del archivo.
     */
    protected abstract List<Metrica> calcularMetricas();
}

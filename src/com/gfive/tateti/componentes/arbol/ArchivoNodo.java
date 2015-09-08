package com.gfive.tateti.componentes.arbol;

import java.nio.file.Path;
import java.util.Objects;

/**
 * Dato para los nodos del árbol de archivos.
 * @author nicolas
 *
 */
public class ArchivoNodo {

    /**
     * Path asociado al ArchivoNodo.
     */
    private Path path;

    /**
     * Construye un ArchivoNodo por path.
     * @param uri
     */
    public ArchivoNodo(Path path) {
        Objects.requireNonNull(path);
        this.path = path;
    }
    
    /**
     * @return la ruta asociada al ArchivoNodo.
     */
    public Path getPath() {
        return path;
    }

    @Override
    public String toString() {
        return path.getFileName().toString();
    }
}

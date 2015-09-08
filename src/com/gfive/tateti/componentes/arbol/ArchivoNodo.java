package com.gfive.tateti.componentes.arbol;

import java.io.File;
import java.net.URI;

/**
 * Archivo que se usa como dato para los nodos del árbol.
 * @author nicolas
 *
 */
public class ArchivoNodo extends File {

    /**
     * ID de serie por defecto.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Construye un ArchivoNodo por ruta.
     * @param uri
     */
    public ArchivoNodo(URI uri) {
        super(uri);
    }

    @Override
    public String toString() {
        return getName();
    }
}

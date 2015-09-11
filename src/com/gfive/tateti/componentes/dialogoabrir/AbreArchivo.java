package com.gfive.tateti.componentes.dialogoabrir;

import java.nio.file.Path;

/**
 * Interfaz implementada por los componentes que necesitan saber cuándo se abre un archivo.
 * 
 * @author nicolas
 *
 */
public interface AbreArchivo {
    
    /**
     * Abre el archivo dado.
     * @param archivo
     */
    public void abrirArchivo(Path archivo);

}

package com.gfive.tateti.componentes.arbol;

import java.io.File;
import java.io.FileFilter;

/**
 * Filtro de archivos que solo acepta carpetas que contengan en algún lado de su jerarquía archivos
 * java.
 * @author nicolas
 *
 */
public class FiltroCarpetas implements FileFilter {

    @Override
    public boolean accept(File archivo) {
        if (archivo.isFile() && archivo.getName().toLowerCase().endsWith(".java"))
            return true;
        if (!archivo.isDirectory())
            return false;
        for (File hijo : archivo.listFiles()) {
            if (accept(hijo))
                return true;
        }
        return false;
    }

}

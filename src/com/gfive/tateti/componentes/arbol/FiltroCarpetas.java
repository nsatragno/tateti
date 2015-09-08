package com.gfive.tateti.componentes.arbol;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Filtro de archivos que solo acepta carpetas que contengan en algún lado de su jerarquía archivos
 * java.
 * @author nicolas
 *
 */
public class FiltroCarpetas {

    /**
     * Verifica que el archivo sea .java o contenga un .java en su jerarquía.
     * @param archivo - el archivo que se quiere verificar.
     * @return true si el archivo verifica, false de lo contrario.
     */
    public boolean matches(Path archivo) {
        if (Files.isRegularFile(archivo) &&
            archivo.getFileName().toString().toLowerCase().endsWith(".java"))
            return true;
        if (!Files.isDirectory(archivo))
            return false;
        try {
            return Files
                .list(archivo)
                .parallel()
                .filter(archivoEncontrado -> matches(archivoEncontrado))
                .findAny()
                .isPresent();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}

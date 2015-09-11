package com.gfive.tateti.componentes.dialogoabrir;

import java.awt.Component;

import javax.swing.JFileChooser;

/**
 * Un diálogo que le permite al usuario abrir una carpeta.
 * @author nicolas
 *
 */
public class DialogoAbrirCarpeta extends JFileChooser {
    
    /**
     * Construye y muestra el diálogo de abrir carpeta. Si se elige abrir un archivo, notifica
     * al AbreArchivo pasado.
     * 
     * @param padre - contenedor del diálogo.
     * @param abreArchivo - objeto que es notificado cuando se abre un archivo.
     */
    public DialogoAbrirCarpeta(Component padre, AbreArchivo abreArchivo) {
        super();
        setFileSelectionMode(DIRECTORIES_ONLY);
        if (showOpenDialog(padre) == APPROVE_OPTION)
            abreArchivo.abrirArchivo(getSelectedFile().toPath());
    }

    /**
     * ID de serie por defecto.
     */
    private static final long serialVersionUID = 1L;

}

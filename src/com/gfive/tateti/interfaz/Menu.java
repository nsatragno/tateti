package com.gfive.tateti.interfaz;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import com.gfive.tateti.componentes.dialogoabrir.AbreArchivo;
import com.gfive.tateti.componentes.dialogoabrir.DialogoAbrir;

/**
 * Barra de menú mostrada en la parte superior derecha de la pantalla.
 * 
 * @author nicolas
 *
 */
public class Menu extends JMenuBar {

    private static final long serialVersionUID = 1L;

    /**
     * Construye un menú, creando todos los items adecuados.
     * @param abreArchivo - objeto que es notificado cuando se abre un archivo nuevo.
     */
    public Menu(AbreArchivo abreArchivo) {
        JMenu archivo = new JMenu("Archivo");
        add(archivo);

        JMenuItem abrirArchivo = new JMenuItem("Abrir...");
        abrirArchivo.addActionListener(
                accion -> new DialogoAbrir(this, abreArchivo, JFileChooser.FILES_ONLY));
        archivo.add(abrirArchivo);

        JMenuItem abrirCarpeta = new JMenuItem("Abrir carpeta...");
        abrirCarpeta.addActionListener(
                accion -> new DialogoAbrir(this, abreArchivo, JFileChooser.DIRECTORIES_ONLY));
        archivo.add(abrirCarpeta);

        JMenuItem salir = new JMenuItem("Salir");
        salir.addActionListener((action) -> System.exit(1));
        archivo.add(salir);
    }
}

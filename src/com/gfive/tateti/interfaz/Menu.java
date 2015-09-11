package com.gfive.tateti.interfaz;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import com.gfive.tateti.componentes.dialogoabrir.AbreArchivo;
import com.gfive.tateti.componentes.dialogoabrir.DialogoAbrirCarpeta;

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

        JMenuItem abrir = new JMenuItem("Abrir...");
        abrir.addActionListener((action) -> System.out.println("Abriendo"));
        archivo.add(abrir);

        JMenuItem abrirCarpeta = new JMenuItem("Abrir carpeta...");
        abrirCarpeta.addActionListener(accion -> new DialogoAbrirCarpeta(this, abreArchivo));
        archivo.add(abrirCarpeta);

        JMenuItem salir = new JMenuItem("Salir");
        salir.addActionListener((action) -> System.out.println("Saliendo"));
        archivo.add(salir);
    }
}

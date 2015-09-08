package com.gfive.tateti.interfaz;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

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
     */
    public Menu() {
        JMenu archivo = new JMenu("Archivo");
        add(archivo);

        JMenuItem abrir = new JMenuItem("Abrir...");
        abrir.addActionListener((action) -> System.out.println("Abriendo"));
        archivo.add(abrir);

        JMenuItem abrirCarpeta = new JMenuItem("Abrir carpeta...");
        abrirCarpeta.addActionListener((action) -> System.out.println("Abriendo carpeta"));
        archivo.add(abrirCarpeta);

        JMenuItem salir = new JMenuItem("Salir");
        salir.addActionListener((action) -> System.out.println("Saliendo"));
        archivo.add(salir);
    }
}

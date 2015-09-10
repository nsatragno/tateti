package com.gfive.tateti.interfaz;

import java.awt.Container;
import java.awt.EventQueue;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.JFrame;
import javax.swing.JTable;

import net.miginfocom.swing.MigLayout;

import com.gfive.tateti.componentes.arbol.ArbolArchivos;
import com.gfive.tateti.componentes.visor.ModeloTablaMetricas;
import com.gfive.tateti.componentes.visor.VisorCodigoFuente;

/**
 * Ventana principal de la aplicación, conteniendo el panel de código y las métricas.
 * 
 * @author nicolas
 *
 */
public class VentanaPrincipal extends JFrame {

    /**
     * ID de serie por defecto.
     */
    private static final long serialVersionUID = 1L;

    /**
     * El nombre de la aplicación mostrado al usuario.
     */
    private static final String NOMBRE_APLICACION = "tateti";

    /**
     * Ruta inicial en la que se buscan los archivos.
     * TODO permitir elegir al usuario.
     */
    private static final Path RUTA_INICIAL = Paths.get("/home/nicolas/Desarrollo/grafos");
    
    /**
     * Arranca la aplicación.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> new VentanaPrincipal());
    }

    /**
     * Inicializa todos los widgets de la ventana principal.
     */
    public VentanaPrincipal() {
        setTitle(NOMBRE_APLICACION);
        setBounds(0, 0, 1024, 768);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setJMenuBar(new Menu());
        setLayout(new MigLayout("", "[30%][70%]", "[80%][20%]"));

        Container pane = getContentPane();

        ArbolArchivos arbol = new ArbolArchivos();
        pane.add(arbol, "grow");
        
        ModeloTablaMetricas modelo = new ModeloTablaMetricas();

        new VisorCodigoFuente(arbol, modelo).agregarAContenedor(pane, "grow, wrap");
        
        pane.add(new JTable(modelo), "span 2, grow, wrap");

        EventQueue.invokeLater(() -> arbol.cargarNodos(RUTA_INICIAL));

        setVisible(true);
    }
}

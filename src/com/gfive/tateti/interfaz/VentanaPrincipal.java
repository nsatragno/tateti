package com.gfive.tateti.interfaz;

import java.awt.Container;
import java.awt.EventQueue;
import java.nio.file.Path;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import net.miginfocom.swing.MigLayout;

import com.gfive.tateti.componentes.arbol.ArbolArchivos;
import com.gfive.tateti.componentes.dialogoabrir.AbreArchivo;
import com.gfive.tateti.componentes.visor.ModeloTablaMetricas;
import com.gfive.tateti.componentes.visor.VisorCodigoFuente;
import com.gfive.tateti.estructuras.HashSetObservable;
/**
 * Ventana principal de la aplicación, conteniendo el panel de código y las métricas.
 * 
 * @author nicolas
 *
 */
public class VentanaPrincipal extends JFrame implements AbreArchivo, HashSetObservable.Observador {

    /**
     * ID de serie por defecto.
     */
    private static final long serialVersionUID = 1L;

    /**
     * El nombre de la aplicación mostrado al usuario.
     */
    private static final String NOMBRE_APLICACION = "tateti";

    /**
     * Árbol que muestra todos los archivos y subcarpetas de la carpeta seleccionada para trabajar
     * en la aplicación.
     */
    private ArbolArchivos arbol;
    
    /**
     * El textarea con resaltado de sintaxis que muestra el código fuente.
     */
    private VisorCodigoFuente visor;

    /**
     * Etiqueta que indica el progreso al cargar un árbol de archivos.
     */
    private JLabel labelProgreso;
    
    /**
     * La cantidad de objetos que han sido cargados en el árbol.
     */
    private int objetosArbol;
    
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
        setJMenuBar(new Menu(this));
        setLayout(new MigLayout("", "[30%][70%]", "[80%][20%]"));

        Container pane = getContentPane();

        arbol = new ArbolArchivos();
        pane.add(new JScrollPane(arbol), "grow");
        
        ModeloTablaMetricas modelo = new ModeloTablaMetricas();

        visor = new VisorCodigoFuente(arbol, modelo);
        visor.agregarAContenedor(pane, "grow, wrap");
        
        pane.add(new JTable(modelo), "span 2, grow, wrap");
        
        labelProgreso = new JLabel();
        pane.add(labelProgreso);

        setVisible(true);
    }

    @Override
    public void abrirArchivo(Path archivo) {
        EventQueue.invokeLater(() -> visor.setText(""));
        EventQueue.invokeLater(() -> labelProgreso.setText("Cargando..."));
        objetosArbol = 0;
        new Thread(() -> arbol.cargarNodos(archivo, this)).start();
    }

    @Override
    public void objetoAgregado() {
        objetosArbol++;
        EventQueue.invokeLater(() ->
            labelProgreso.setText(
                    objetosArbol + " archivo" + (objetosArbol > 1 ? "s" : "") + " cargados"));
    }
}

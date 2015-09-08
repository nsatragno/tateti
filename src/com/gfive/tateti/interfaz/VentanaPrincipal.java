package com.gfive.tateti.interfaz;

import java.awt.Container;
import java.awt.EventQueue;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.JFrame;

import net.miginfocom.swing.MigLayout;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

import com.gfive.tateti.componentes.arbol.ArbolArchivos;

/**
 * Ventana principal de la aplicación, conteniendo el panel de código y las métricas.
 * 
 * @author nicolas
 *
 */
public class VentanaPrincipal extends JFrame {

    private static final long serialVersionUID = 1L;

    /**
     * El nombre de la aplicación mostrado al usuario.
     */
    private static final String NOMBRE_APLICACION = "tateti";

    private static final Path RUTA_INICIAL = Paths.get("/home/nicolas/Desarrollo/grafos");

    /**
     * Arranca la aplicación.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                VentanaPrincipal ventana = new VentanaPrincipal();
                ventana.setVisible(true);
            }
        });
    }

    /**
     * Inicializa todos los widgets de la ventana principal.
     */
    public VentanaPrincipal() {
        setTitle(NOMBRE_APLICACION);
        setBounds(0, 0, 1024, 768);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setJMenuBar(new Menu());
        setLayout(new MigLayout("debug", "[][grow]", "[grow]"));
        //setLayout(new MigLayout("debug", "", ""));

        Container pane = getContentPane();

        ArbolArchivos arbol = new ArbolArchivos();
        arbol.cargarNodos(RUTA_INICIAL);
        pane.add(arbol, "width 30%, growy");

        RSyntaxTextArea textArea = new RSyntaxTextArea();
        textArea.setEditable(false);
        textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
        RTextScrollPane scrollPane = new RTextScrollPane(textArea);
        pane.add(scrollPane, "grow, wrap");
    }
}

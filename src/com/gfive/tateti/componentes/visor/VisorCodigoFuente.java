package com.gfive.tateti.componentes.visor;

import java.awt.Container;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Objects;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

import com.gfive.tateti.componentes.arbol.ArbolArchivos;
import com.gfive.tateti.componentes.arbol.NodoArbol;
import com.gfive.tateti.log.Log;

/**
 * Extensión del editor de código fuente RSyntaxTextArea.
 * @author nicolas
 *
 */
public class VisorCodigoFuente extends RSyntaxTextArea implements TreeSelectionListener {

    /**
     * ID de serialización por defecto.
     */
    private static final long serialVersionUID = 1L;
    
    /**
     * Panel de desplazamiento que contiene al textarea.
     */
    private final RTextScrollPane scrollPane;
    
    /**
     * El árbol de archivos al que el visor está relacionado.
     */
    private final ArbolArchivos arbol;

    // TODO mover a otro lado.
    private ModeloTablaMetricas modelo;

    /**
     * Construye un visor de código fuente asociado a un árbol de archivos dado.
     * @param arbol - el árbol al que el visor está relacionado. No puede ser null.
     */
    public VisorCodigoFuente(ArbolArchivos arbol, ModeloTablaMetricas modelo) {
        this.modelo = modelo;
        Objects.requireNonNull(arbol);
        this.arbol = arbol;

        scrollPane = new RTextScrollPane(this);

        setEditable(false);
        setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);

        arbol.addTreeSelectionListener(this);
    }
    
    /**
     * Agrega el visor al contenedor dado.
     * @param container - objeto que contendrá al visor.
     * @param constraints - parámetros de posicionamiento pasados al LayoutManager.
     */
    public void agregarAContenedor(Container container, String constraints) {
        container.add(scrollPane, constraints);
    }

    @Override
    public void valueChanged(TreeSelectionEvent evento) {
        // La selección del árbol cambió. Buscamos cuál es el nuevo elemento seleccionado.
        NodoArbol nodo = arbol.getLastSelectedPathComponent();

        // Si la nueva selección está vacía, no hay nada que hacer.
        if (nodo == null)
            return;
        
        // TODO mover el modelo a otro lado.
        modelo.setMetricas(nodo.calcularMetricas());
        
        // Si seleccionó una carpeta, tampoco hay nada que hacer.
        if (nodo.esCarpeta())
            return;

        try {
            setText("");
            Files
                .lines(nodo.getRutaArchivo())
                .forEach((linea) -> append(linea + "\n"));
        } catch (IOException e) {
            Log log = new Log();
            log.reportarError(e);
            setText("No se pudo abrir el archivo: " + e.getMessage());
        }
        
        // Scrolleamos hacia la parte superior del archivo.
        setCaretPosition(0);
    }
}

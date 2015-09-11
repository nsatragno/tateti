package com.gfive.tateti.componentes.arbol;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

public class ArbolArchivos extends JTree {

    /**
     * ID de serie por defecto.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Construye el árbol de archivos sin datos.
     */
    public ArbolArchivos() {
        super(new DefaultMutableTreeNode());
        getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        setRootVisible(false);
    }

    /**
     * Carga recursivamente en el árbol la estructura del sistema de archivos a partir del path
     * dado.
     * 
     * @param rutaInicial
     *            - la ruta donde se empieza a examinar el sistema de archivos.
     */
    public void cargarNodos(Path rutaInicial) {
        // Cambiar el setRootVisible es un workaround para un bug de swing.
        setRootVisible(true);
        getRaiz().removeAllChildren();
        llenarArbol(getRaiz(), rutaInicial);
        
        updateUI();

        // Muestro todas las filas expandidas.
        for (int i = 0; i < getRowCount(); i++)
            expandRow(i);

        setRootVisible(false);
    }

    /**
     * Recorre el sistema de archivos, buscando archivos .java y carpetas para agregar al árbol,
     * hijos de la ruta pasada.
     * 
     * @param predecesor
     *            - nodo padre del subárbol que se creará.
     * @param rutaNueva
     *            - archivo del que se parte para llenar el árbol.
     */
    private void llenarArbol(DefaultMutableTreeNode predecesor, Path rutaNueva) {
        NodoArbol nodo = NodoArbol.construir(rutaNueva);
        predecesor.add(nodo);

        if (!nodo.esCarpeta())
            return;

        FiltroCarpetas filtro = new FiltroCarpetas();
        try {
            Files
                .list(nodo.getRutaArchivo())
                .parallel()
                .filter(hijo -> filtro.matches(hijo))
                .forEach(hijo -> llenarArbol(nodo, hijo));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @return el nodo raíz del árbol.
     */
    public DefaultMutableTreeNode getRaiz() {
        return (DefaultMutableTreeNode) getModel().getRoot();
    }
    
    @Override
    public NodoArbol getLastSelectedPathComponent() {
        return (NodoArbol) super.getLastSelectedPathComponent();
    }
}

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
    }

    /**
     * Carga recursivamente en el árbol la estructura del sistema de archivos a partir del path
     * dado.
     * 
     * @param rutaInicial
     *            - la ruta donde se empieza a examinar el sistema de archivos.
     */
    public void cargarNodos(Path rutaInicial) {
        ArchivoNodo archivoRaiz = new ArchivoNodo(rutaInicial);

        llenarArbol(getRaiz(), archivoRaiz);

        // Muestro todas las filas expandidas.
        for (int i = 0; i < getRowCount(); i++)
            expandRow(i);
    }

    /**
     * Recorre el sistema de archivos, buscando carpetas para agregar al árbol.
     * 
     * @param raiz
     *            - nodo raíz del árbol.
     * @param archivoRaiz
     *            - archivo del que se parte para llenar.
     */
    private void llenarArbol(DefaultMutableTreeNode raiz, ArchivoNodo archivoRaiz) {
        DefaultMutableTreeNode nodo = new DefaultMutableTreeNode(archivoRaiz);
        raiz.add(nodo);

        if (!Files.isDirectory(archivoRaiz.getPath()))
            return;

        FiltroCarpetas filtro = new FiltroCarpetas();
        try {
            Files
                .list(archivoRaiz.getPath())
                .filter(hijo -> filtro.matches(hijo))
                .forEach(hijo -> llenarArbol(nodo, new ArchivoNodo(hijo)));
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
}

package com.gfive.tateti.componentes.arbol;

import java.io.File;
import java.nio.file.Path;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

public class ArbolArchivos extends JTree {

    /**
     * ID de serie por defecto.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Mensaje que se muestra al usuario cuando la ruta pasada no es una carpeta en el sistema de
     * archivos.
     */
    private static final String MENSAJE_RAIZ_NO_ES_CARPETA = "La ruta seleccionada no es una carpeta";

    /**
     * Construye el árbol de archivos sin datos.
     */
    public ArbolArchivos() {
        super(new DefaultMutableTreeNode());
    }

    /**
     * Carga recursivamente en el árbol la estructura del sistema de archivos a partir del path
     * dado.
     * 
     * @param rutaInicial
     *            - la ruta donde se empieza a examinar el sistema de archivos.
     */
    public void cargarNodos(Path rutaInicial) {
        ArchivoNodo archivoRaiz = new ArchivoNodo(rutaInicial.toUri());

        llenarArbol(getRaiz(), archivoRaiz);

        // Muestro la primera fila expandida para que el árbol no se vea todo colapsado.
        expandRow(0);
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

        if (!archivoRaiz.isDirectory())
            return;

        for (File hijo : archivoRaiz.listFiles(new FiltroCarpetas()))
            llenarArbol(nodo, new ArchivoNodo(hijo.toURI()));
    }

    /**
     * @return el nodo raíz del árbol.
     */
    public DefaultMutableTreeNode getRaiz() {
        return (DefaultMutableTreeNode) getModel().getRoot();
    }
}

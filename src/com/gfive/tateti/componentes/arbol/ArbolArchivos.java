package com.gfive.tateti.componentes.arbol;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
        
        Map<Path, Path> archivosQueLlenar = new ConcurrentHashMap<Path, Path>();
        marcarArchivosParaCargar(rutaInicial, archivosQueLlenar);

        llenarArbol(getRaiz(), rutaInicial, archivosQueLlenar);
        
        updateUI();

        // Muestro todas las filas expandidas.
        for (int i = 0; i < getRowCount(); i++)
            expandRow(i);

        setRootVisible(false);
    }
    
    /**
     * Actualiza el conjunto de todos los archivos que deberían cargarse en el árbol de la
     * aplicación.
     * 
     * @param archivo
     * @param conjunto
     */
    private boolean marcarArchivosParaCargar(Path archivo, Map<Path, Path> conjunto) {
        if (Files.isRegularFile(archivo) &&
            archivo.getFileName().toString().toLowerCase().endsWith(".java")) {
            conjunto.put(archivo, archivo);
            return true;
        }
        if (!Files.isDirectory(archivo))
            return false;

        try {
            boolean marcada =  Files.list(archivo)
                 .parallel()
                 .map((hijo) -> marcarArchivosParaCargar(hijo, conjunto))
                 .reduce(false, (r1, r2) -> r1 == true || r2 == true);
            if (marcada)
                conjunto.put(archivo, archivo);
            return marcada;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * Recorre el sistema de archivos, buscando archivos .java y carpetas para agregar al árbol,
     * hijos de la ruta pasada.
     * 
     * @param predecesor
     *            - nodo padre del subárbol que se creará.
     * @param rutaNueva
     *            - archivo del que se parte para llenar el árbol.
     * @param archivosQueLlenar 
     */
    private void llenarArbol(DefaultMutableTreeNode predecesor,
                             Path rutaNueva,
                             Map<Path, Path> archivosQueLlenar) {
        NodoArbol nodo = NodoArbol.construir(rutaNueva);
        predecesor.add(nodo);

        if (!nodo.esCarpeta())
            return;

        try {
            Files
                .list(nodo.getRutaArchivo())
                .parallel()
                .filter(hijo -> archivosQueLlenar.containsKey(hijo))
                .forEach(hijo -> llenarArbol(nodo, hijo, archivosQueLlenar));
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

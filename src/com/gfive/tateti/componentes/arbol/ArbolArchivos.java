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
     * Carga recursivamente en el árbol la estructura del sistema de archivos, actualizando la
     * vista.
     * 
     * @param rutaInicial
     *            - la ruta donde se empieza a examinar el sistema de archivos.
     */
    public void cargarNodos(Path rutaInicial) {
        // Cambiar el setRootVisible es un workaround para un bug de swing.
        setRootVisible(true);
        getRaiz().removeAllChildren();
        
        ConcurrentHashMap<Path, Path> archivosQueLlenar = new ConcurrentHashMap<Path, Path>();
        marcarArchivosParaCargar(rutaInicial, archivosQueLlenar);

        llenarArbol(getRaiz(), rutaInicial, archivosQueLlenar);
        
        updateUI();

        // Muestro todas las filas expandidas.
        for (int i = 0; i < getRowCount(); i++)
            expandRow(i);

        setRootVisible(false);
    }
    
    /**
     * Recorre el árbol de archivos a partir del archivo dado. Si el archivo es .java o es una
     * carpeta con archivos .java en alguna parte de su jerarquía, la agrega al conjunto pasado.
     * 
     * @param archivo - archivo del que se parte para marcar los archivos.
     * @param conjunto - mapa en el que se van guardando los archivos que se marcan para inserción
     *                   en el árbol.
     * @return true si agregó el archivo pasado al árbol, false de lo contrario.
     */
    private boolean marcarArchivosParaCargar(Path archivo, ConcurrentHashMap<Path, Path> conjunto) {
        if (Files.isRegularFile(archivo) &&
            archivo.getFileName().toString().toLowerCase().endsWith(".java")) {
            // Si es un archivo normal y .java, lo agregamos al conjunto y ya terminamos.
            conjunto.put(archivo, archivo);
            return true;
        }
        if (!Files.isDirectory(archivo)) {
            // El archivo no es una carpeta, y no es .java. No nos interesa.
            return false;
        }

        try {
            // Recorremos todos los hijos, llamando recursivamente a esta función. Si alguno
            // se agrega, agregamos a este archivo también.
            boolean marcada =  Files.list(archivo)
                 .parallel()
                 .map((hijo) -> marcarArchivosParaCargar(hijo, conjunto))
                 // La reducción se asegura que, con que uno devuelva true, el resultado sea true.
                 // Evitamos usar anyMatch() para asegurarnos que se recorran todos los elementos
                 // del stream.
                 .reduce(false, (r1, r2) -> r1 == true || r2 == true);
            if (marcada)
                conjunto.put(archivo, archivo);
            return marcada;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * Recorre el sistema de archivos, buscando archivos para agregar al árbol, hijos de la ruta
     * pasada contenidos el archivosQueLlenar.
     * 
     * @param predecesor
     *            - nodo padre del subárbol que se creará.
     * @param archivo
     *            - archivo del que se parte para llenar el árbol.
     * @param archivosQueLlenar
     *            - mapa con los archivos que deben insertarse en el árbol.
     */
    private void llenarArbol(DefaultMutableTreeNode predecesor,
                             Path archivo,
                             Map<Path, Path> archivosQueLlenar) {
        NodoArbol nodo = NodoArbol.construir(archivo);
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

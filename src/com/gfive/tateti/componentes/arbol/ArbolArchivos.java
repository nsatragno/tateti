package com.gfive.tateti.componentes.arbol;

import java.awt.EventQueue;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.stream.Stream;

import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

import com.gfive.tateti.estructuras.HashSetObservable;

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
     * @param observador
     *            - objeto al que se le notifican las inserciones de archivos.
     */
    public void cargarNodos(Path rutaInicial, HashSetObservable.Observador observador) {
        // Cambiar el setRootVisible es un workaround para un bug de swing.
        EventQueue.invokeLater(() -> setRootVisible(true)); 

        getRaiz().removeAllChildren();
        
        HashSetObservable<Path> archivosQueLlenar = new HashSetObservable<Path>(observador);
        marcarArchivosParaCargar(rutaInicial, archivosQueLlenar);
        llenarArbol(getRaiz(), rutaInicial, archivosQueLlenar);
        
        EventQueue.invokeLater(() -> updateUI()); 
        EventQueue.invokeLater(() -> { for (int i = 0; i < getRowCount(); i++) expandRow(i); }); 
        EventQueue.invokeLater(() -> setRootVisible(false)); 
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
    private boolean marcarArchivosParaCargar(Path archivo, HashSetObservable<Path> conjunto) {
        if (Files.isRegularFile(archivo) &&
            archivo.getFileName().toString().toLowerCase().endsWith(".java")) {
            // Si es un archivo normal y .java, lo agregamos al conjunto y ya terminamos.
            conjunto.put(archivo);
            return true;
        }
        if (!Files.isDirectory(archivo)) {
            // El archivo no es una carpeta, y no es .java. No nos interesa.
            return false;
        }

        // Recorremos todos los hijos, llamando recursivamente a esta función. Si alguno
        // se agrega, agregamos a este archivo también.
        try (Stream<Path> stream = Files.list(archivo)) {
             boolean marcada = stream
                 .parallel()
                 .map((hijo) -> marcarArchivosParaCargar(hijo, conjunto))
                 // La reducción se asegura que, con que uno devuelva true, el resultado sea true.
                 // Evitamos usar anyMatch() para asegurarnos que se recorran todos los elementos
                 // del stream.
                 .reduce(false, (r1, r2) -> r1 == true || r2 == true);
            if (marcada)
                conjunto.put(archivo);
            return marcada;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
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

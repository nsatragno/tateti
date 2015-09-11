package com.gfive.tateti.componentes.arbol;

import java.nio.file.Path;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.gfive.tateti.metricas.Metrica;

/**
 * Nodo del árbol de archivos que representa una carpeta.
 * @author nicolas
 *
 */
public class NodoCarpeta extends NodoArbol {

    /**
     * ID de serie por defecto.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Construye un NodoCarpeta.
     * @param path
     */
    protected NodoCarpeta(Path path) {
        super(path);
    }

    @Override
    public boolean esCarpeta() {
        return true;
    }

    @Override
    public List<Metrica> calcularMetricas() {
        @SuppressWarnings("unchecked")
        Vector<NodoArbol> hijos = children;
        return hijos.stream()
             .map(hijo -> hijo.calcularMetricas())
             .reduce((lista1, lista2) -> reducirMetricas(lista1, lista2))
             .get();
    }
    
    /**
     * Fusiona los dos listados de métricas en uno solo.
     * 
     * @param listaMetrica1
     * @param listaMetrica2
     * @return un listado nuevo consistente en agregar elemento a elemento de cada listado de
     * métricas.
     */
    private List<Metrica> reducirMetricas(List<Metrica> listaMetrica1, List<Metrica> listaMetrica2) {
        if (listaMetrica1.size() != listaMetrica2.size())
            throw new InvalidParameterException(
                    "El tamaño de las dos listas de métricas debe ser el mismo");

        ArrayList<Metrica> listaFusionada = new ArrayList<>(listaMetrica1.size());
        for (int i = 0; i < listaMetrica1.size(); i++)
            listaFusionada.add(listaMetrica1.get(i).agregar(listaMetrica2.get(i)));
        
        return listaFusionada;
    }

}

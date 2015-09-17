package com.gfive.tateti.estructuras;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Un ConcurrentHashSet que permite observar las inserciones.
 * @author nicolas
 *
 */
public class HashSetObservable<T> extends ConcurrentHashMap<T, T> {

    /**
     * ID de serie por defecto.
     */
    private static final long serialVersionUID = 1L;
    
    /**
     * Objeto al que se le notifican las inserciones.
     */
    private Observador observador;
    
    /**
     * Interfaz para los observadores del hashset.
     * @author nicolas
     *
     */
    public interface Observador {
        /**
         * Indica que se ha agregado un objeto.
         */
        public void objetoAgregado();
    }
    
    public HashSetObservable(Observador observador) {
        this.observador = observador;
    }

    /**
     * Inserta un elemento en el hashset.
     * @param value - el valor que insertar.
     */
    public void put(T value) {
        synchronized (this) {
            observador.objetoAgregado();
        }
        super.put(value, value);
    }
}

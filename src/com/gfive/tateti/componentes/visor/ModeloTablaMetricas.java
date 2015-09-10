package com.gfive.tateti.componentes.visor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.swing.table.AbstractTableModel;

import com.gfive.tateti.metricas.Metrica;

/**
 * Llena la tabla de métricas.
 * @author nicolas
 *
 */
public class ModeloTablaMetricas extends AbstractTableModel {
    
    /**
     * Listado de métricas mostrado. Puede vacío pero no null.
     */
    private List<Metrica> metricas;

    /**
     * ID de serie genérico.
     */
    private static final long serialVersionUID = 1L;
    
    /**
     * Construye el modelo de métricas.
     */
    public ModeloTablaMetricas() {
        metricas = new ArrayList<Metrica>();
    }
    
    /**
     * Establece las métricas mostradas por el modelo.
     * @param metricas - listado de métricas. No puede ser null.
     * @throws NullPointerException si las métricas son null.
     */
    public void setMetricas(List<Metrica> metricas) {
        Objects.requireNonNull(metricas);
        this.metricas = metricas;
        fireTableStructureChanged();
    }

    @Override
    public int getRowCount() {
        return metricas.size();
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public Object getValueAt(int fila, int columna) {
        if (columna == 0)
            return metricas.get(fila).getNombre();
        return metricas.get(fila).getValor();
    }
}

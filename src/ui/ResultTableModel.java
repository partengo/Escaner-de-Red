package src.ui;

import src.app.PingResult;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class ResultTableModel extends AbstractTableModel {
    private final String[] columnas = {"IP", "Host", "Activo", "Tiempo (ms)"};
    private final List<PingResult> resultados = new ArrayList<>();

    public void agregarResultado(PingResult r) {
        resultados.add(r);
        fireTableRowsInserted(resultados.size() - 1, resultados.size() - 1);
    }

    public void limpiar() {
        resultados.clear();
        fireTableDataChanged();
    }

    public List<PingResult> getResultados() {
        return resultados;
    }

    @Override
    public int getRowCount() {
        return resultados.size();
    }

    @Override
    public int getColumnCount() {
        return columnas.length;
    }

    @Override
    public String getColumnName(int col) {
        return columnas[col];
    }

    @Override
    public Object getValueAt(int row, int col) {
        PingResult r = resultados.get(row);
        return switch (col) {
            case 0 -> r.getIp();
            case 1 -> r.getHost();
            case 2 -> r.isActivo() ? "Sí" : "No";
            case 3 -> r.getTiempoRespuesta();
            default -> "";
        };
    }
}


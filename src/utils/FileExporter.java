package src.utils;

import src.app.PingResult;

import javax.swing.*;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.List;

public class FileExporter {
    public static void exportarCSV(List<PingResult> resultados) {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            try (PrintWriter pw = new PrintWriter(new FileWriter(chooser.getSelectedFile()))) {
                pw.println("IP,Host,Activo,Tiempo(ms)");
                for (PingResult r : resultados) {
                    pw.printf("%s,%s,%s,%d\n",
                            r.getIp(),
                            r.getHost(),
                            r.isActivo() ? "Sí" : "No",
                            r.getTiempoRespuesta());
                }
                JOptionPane.showMessageDialog(null, "Archivo guardado correctamente.");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error al guardar archivo: " + e.getMessage());
            }
        }
    }
}
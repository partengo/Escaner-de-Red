package src.utils;

import src.app.PingResult;
import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class FileExporter {

    public static void exportarCSV(List<PingResult> resultados) {
        try {
            // Ruta base del proyecto
            String rutaProyecto = new File("").getAbsolutePath();

            // Carpeta "exports" dentro del proyecto
            File carpetaExports = new File(rutaProyecto, "exports");
            if (!carpetaExports.exists()) {
                carpetaExports.mkdirs(); // Crear si no existe
            }

            // Nombre del archivo con fecha y hora
            String fechaHora = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
            File archivoSalida = new File(carpetaExports, "Escaneo_" + fechaHora + ".csv");

            // Escribir datos en el archivo
            try (PrintWriter pw = new PrintWriter(new FileWriter(archivoSalida))) {
                pw.println("IP,Host,Activo,Tiempo(ms)");
                for (PingResult r : resultados) {
                    pw.printf("%s,%s,%s,%d\n",
                            r.getIp(),
                            r.getHost(),
                            r.isActivo() ? "Sí" : "No",
                            r.getTiempoRespuesta());
                }
            }

            // Confirmar guardado
            JOptionPane.showMessageDialog(null,
                    "Archivo guardado automáticamente en:\n" + archivoSalida.getAbsolutePath());

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al guardar archivo: " + e.getMessage());
        }
    }
}

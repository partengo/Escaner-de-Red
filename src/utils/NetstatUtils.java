package src.utils;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class NetstatUtils {

    private static String ejecutarComando(String comando) {
        StringBuilder resultado = new StringBuilder();
        try {
            Process proc = Runtime.getRuntime().exec(comando);
            BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String linea;
            while ((linea = reader.readLine()) != null) {
                resultado.append(linea).append("\n");
            }
            reader.close();
            proc.waitFor();
        } catch (Exception e) {
            return "Error ejecutando comando: " + e.getMessage();
        }
        return resultado.toString();
    }

    public static String mostrarConexiones() {
        String so = System.getProperty("os.name").toLowerCase();
        String comando = so.contains("win") ? "netstat -a" : "netstat -an";
        return ejecutarComando(comando);
    }

    public static String mostrarConexionesNumericas() {
        String so = System.getProperty("os.name").toLowerCase();
        String comando = so.contains("win") ? "netstat -n" : "netstat -an";
        return ejecutarComando(comando);
    }

    public static String mostrarConexionesConProcesos() {
        String so = System.getProperty("os.name").toLowerCase();
        String comando = so.contains("win") ? "netstat -o" : "netstat -p";
        return ejecutarComando(comando);
    }
}
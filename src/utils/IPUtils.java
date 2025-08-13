package src.utils;

import java.util.ArrayList;
import java.util.List;

public class IPUtils {
    public static boolean validarIP(String ip) {
        return ip.matches("^((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}" +
                          "(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)$");
    }

    public static List<String> generarRango(String ipInicio, String ipFin) {
        long start = ipToLong(ipInicio);
        long end = ipToLong(ipFin);
        List<String> lista = new ArrayList<>();

        for (long i = start; i <= end; i++) {
            lista.add(longToIp(i));
        }
        return lista;
    }

    private static long ipToLong(String ip) {
        String[] partes = ip.split("\\.");
        long resultado = 0;
        for (String parte : partes) {
            resultado = (resultado << 8) + Integer.parseInt(parte);
        }
        return resultado;
    }

    private static String longToIp(long ip) {
        return String.format("%d.%d.%d.%d",
                (ip >> 24) & 0xff,
                (ip >> 16) & 0xff,
                (ip >> 8) & 0xff,
                ip & 0xff);
    }
}
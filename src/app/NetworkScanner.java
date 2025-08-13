package src.app;

public class NetworkScanner {

    public static PingResult escanearIP(String ip) {
    long inicio = System.currentTimeMillis();
    boolean activo = false;
    String host = "N/A";

    try {
        String so = System.getProperty("os.name").toLowerCase();
        String comando;

        if (so.contains("win")) {
            comando = "ping -n 1 -w 1000 " + ip;
        } else {
            comando = "ping -c 1 -W 1 " + ip;
        }

        Process proc = Runtime.getRuntime().exec(comando);
        int exitVal = proc.waitFor();
        activo = (exitVal == 0);
    } catch (Exception e) {
        activo = false;
    }

    if (activo) {
        try {
            host = java.net.InetAddress.getByName(ip).getCanonicalHostName();
        } catch (Exception ignored) {}
    }

    long duracion = System.currentTimeMillis() - inicio;

    return new PingResult(ip, host, activo, duracion);
    }
}
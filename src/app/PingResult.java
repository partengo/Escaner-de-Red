package src.app;

public class PingResult {
    private String ip;
    private String host;
    private boolean activo;
    private long tiempoRespuesta;

    public PingResult(String ip, String host, boolean activo, long tiempoRespuesta) {
        this.ip = ip;
        this.host = host;
        this.activo = activo;
        this.tiempoRespuesta = tiempoRespuesta;
    }

    public String getIp() { return ip; }
    public String getHost() { return host; }
    public boolean isActivo() { return activo; }
    public long getTiempoRespuesta() { return tiempoRespuesta; }
}
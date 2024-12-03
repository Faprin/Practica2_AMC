package Controlador;

public class ResultadoVersus {

    private int talla;
    private String vencedor;
    private double tiempoUnidireccional;
    private double tiempoBidireccional;

    public ResultadoVersus(int talla, String vencedor, double tiempoUnidireccional, double tiempoBidireccional) {
        this.talla = talla;
        this.vencedor = vencedor;
        this.tiempoUnidireccional = tiempoUnidireccional;
        this.tiempoBidireccional = tiempoBidireccional;
    }

    public int getTalla() {
        return talla;
    }

    public String getVencedor() {
        return vencedor;
    }

    public double getTiempoUnidireccional() {
        return tiempoUnidireccional;
    }

    public double getTiempoBidireccional() {
        return tiempoBidireccional;
    }
}

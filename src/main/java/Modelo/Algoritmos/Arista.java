package Modelo.Algoritmos;

public class Arista {

    private Punto origen;
    private Punto destino;
    private double distancia;

    public Arista() {

    }

    public Arista(Punto origen, Punto destino, double distancia) {
        this.origen = origen;
        this.destino = destino;
        this.distancia = distancia;
    }

    public double getDistancia() {
        return this.distancia;
    }

    public Punto getOrigen() {
        return origen;
    }

    public void setOrigen(Punto origen) {
        this.origen = origen;
    }

    public void setDestino(Punto destino) {
        this.destino = destino;
    }

    public void setDistancia(double distancia) {
        this.distancia = distancia;
    }

    public Punto getDestino() {
        return destino;
    }
}

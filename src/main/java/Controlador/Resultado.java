package Controlador;

import Modelo.Algoritmos.Arista;
import Modelo.Algoritmos.Punto;

import java.util.ArrayList;
import java.util.List;

public class Resultado {

    private String nombre;
    private double distancia;
    private int calculadas;
    private double tiempo;
    private int talla;

    private ArrayList<Arista> aristas;

    private List<Punto> camino;

    public Resultado() {
    }

    public Resultado(String nombre, double distancia, int calculadas, double tiempo, List<Punto> camino) {
        this.nombre = nombre;
        this.distancia = distancia;
        this.calculadas = calculadas;
        this.tiempo = tiempo;
        this.camino = camino;
    }

    public Resultado(String nombre, double distancia, int calculadas, double tiempo, ArrayList<Arista> aristas) {
        this.nombre = nombre;
        this.distancia = distancia;
        this.calculadas = calculadas;
        this.tiempo = tiempo;
        this.aristas = new ArrayList<>(aristas);
    }

    public int getTalla() {
        return talla;
    }

    public void setTalla(int talla) {
        this.talla = talla;
    }

    public String getNombre() {
        return nombre;
    }

    public double getDistancia() {
        return distancia;
    }

    public int getCalculadas() {
        return calculadas;
    }

    public double getTiempo() {
        return tiempo;
    }

    public ArrayList<Punto> getCamino() {
        return new ArrayList<>(camino);
    }

    public ArrayList<Arista> getAristas() { return this.aristas; }


}

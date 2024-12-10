package Controlador;

import Modelo.Algoritmos.Punto;

import java.util.ArrayList;
import java.util.List;

public class Dimensionado {

    private double escala;

    public Dimensionado(double anchoContenedor, double altoContenedor, List<Punto> puntos) {
        calcularEscala(anchoContenedor, altoContenedor, puntos);
    }

    private void calcularEscala(double ancho, double alto, List<Punto> puntos) {

        double maxX = 0, maxY = 0;
        for(Punto p : puntos) {
            if(p.getX() > maxX) {
                maxX = p.getX();
            }
            if(p.getY() > maxY) {
                maxY = p.getY();
            }
        }

        double escalaX = ancho / maxX;
        double escalaY = alto / maxY;

        this.escala = Math.min(escalaX, escalaY);
    }

    public Punto redimensionar(Punto punto) {
        return new Punto((int) (punto.getX() * escala), (int) (punto.getY() * escala), punto.getId());
    }

    public double getEscala() {
        return escala;
    }

}
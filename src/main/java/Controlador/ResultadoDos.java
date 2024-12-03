package Controlador;

public class ResultadoDos {
    private int talla;
    private double tiempo1;
    private double tiempo2;
    private int calculadas1;
    private int calculadas2;

    public ResultadoDos(int talla, double tiempo1, double tiempo2, int calculadas1, int calculadas2) {
        this.talla = talla;
        this.tiempo1 = tiempo1;
        this.tiempo2 = tiempo2;
        this.calculadas1 = calculadas1;
        this.calculadas2 = calculadas2;
    }

    public int getTalla() {
        return talla;
    }

    public double getTiempo1() {
        return tiempo1;
    }

    public double getTiempo2() {
        return tiempo2;
    }

    public int getCalculadas1() {
        return calculadas1;
    }

    public int getCalculadas2() {
        return calculadas2;
    }
}

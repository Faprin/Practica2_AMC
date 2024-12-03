package Modelo.Algoritmos;

public class Punto {

    private double x,
            y;
    private int id;

    public Punto(double x, double y, int id) {
        this.x = x;
        this.y = y;
        this.id = id;
    }

    public Punto() {}

    // getters
    public double getX() { return this.x; }
    public double getY() { return this.y; }
    public int getId() { return this.id; }

    // setters
    public void setId(int id) { this.id = id; }
    public void setX(double x) { this.x = x; }
    public void setY(double y) { this.y = y; }

    @Override
    public String toString() {
        return new String(this.id + " (" + this.getX() + ", " + this.getY() + ")");
    }
}


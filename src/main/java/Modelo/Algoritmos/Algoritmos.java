package Modelo.Algoritmos;

import Controlador.Resultado;

import java.util.ArrayList;
import java.util.Random;


public class Algoritmos {

    private static Random random = new Random(System.nanoTime());
    private static int calculdadas = 0;
    private static int ciudadPartida;
    private static double distanciaTotal;
    private static int puntoPartida = -1;

    public static void setDistanciaTotal() {
        distanciaTotal = 0;
    }

    public static double getDistanciaTotal() {
        return Math.round(distanciaTotal * 100.0) / 100.0;
    }

    public static void setCalculadasToZero() {
        calculdadas = 0;
    }

    public static int getCalculadas() {
        return calculdadas;
    }

    public static void setCiudadPartida(int dimension) {
        if(puntoPartida == -1) {
            ciudadPartida = random.nextInt(dimension); // modo automático
        } else {
            ciudadPartida = puntoPartida; // modo manual
        }
    }

    public static void setPuntoPartida(int punto) {
        puntoPartida = punto;
    }

    public static int getCiudadPartida() {
        return ciudadPartida;
    }

    public static double distancia(Punto a, Punto b) {
        calculdadas++;
        return (Math.sqrt((b.getX() - a.getX()) * (b.getX() - a.getX()) + (b.getY() - a.getY()) * (b.getY() - a.getY())));
    }

    public static void setParameters() {
        Algoritmos.setCalculadasToZero();
        Algoritmos.setDistanciaTotal();
    }

    public static Resultado exhaustivoUnidireccional(ArrayList<Punto> ciudades) {
        Algoritmos.setParameters();
        double start = System.nanoTime();

        ArrayList<Punto> ciudadesVisitadas = new ArrayList<>();
        ArrayList<Arista> rutaAristas = new ArrayList<>();

        Punto ciudadActual = ciudades.get(Algoritmos.getCiudadPartida());
        ciudadesVisitadas.add(ciudadActual);

        while (ciudadesVisitadas.size() < ciudades.size()) {
            double minDistance = Double.MAX_VALUE;
            Punto ciudadDestino = null;

            for (Punto destino : ciudades) {
                if (!ciudadesVisitadas.contains(destino)) {
                    double dist = distancia(ciudadActual, destino);
                    if (dist < minDistance) {
                        minDistance = dist;
                        ciudadDestino = destino;
                    }
                }
            }
            // hago la elección y vuelvo a iterar
            ciudadesVisitadas.add(ciudadDestino);
            Algoritmos.distanciaTotal += minDistance;
            rutaAristas.add(new Arista(ciudadActual, ciudadDestino, minDistance));
            ciudadActual = ciudadDestino;
        }

        // volvemos al inicio
        double distanciaVuelta = distancia(ciudadesVisitadas.getLast(), ciudadesVisitadas.getFirst());
        Algoritmos.distanciaTotal += distanciaVuelta;
        rutaAristas.add(new Arista(ciudadesVisitadas.getLast(), ciudadesVisitadas.getFirst(), distanciaVuelta));

        double end = System.nanoTime();
        return new Resultado("Exhaustiva Unidireccional", Algoritmos.getDistanciaTotal(), Algoritmos.getCalculadas(), (end - start) / 1e6, rutaAristas);
    }

    public static Resultado podaUnidireccional(ArrayList<Punto> ciudades) {
        double start = System.nanoTime();
        Algoritmos.setParameters();

        ArrayList<Punto> ciudadesVisitadas = new ArrayList<>();
        ArrayList<Arista> rutaAristas = new ArrayList<>();

        Punto ciudadActual = ciudades.get(Algoritmos.getCiudadPartida());
        ciudadesVisitadas.add(ciudadActual);

        Algoritmos.quickSort(ciudades, 'x');

        while (ciudadesVisitadas.size() < ciudades.size()) {
            double minDistance = Double.MAX_VALUE;
            Punto ciudadDestino = null;

            for (Punto destino : ciudades) {
                if (!ciudadesVisitadas.contains(destino)) {
                    if (Math.abs(ciudadActual.getX() - destino.getX()) < minDistance) {
                        double dist = distancia(ciudadActual, destino);
                        if (dist < minDistance) {
                            minDistance = dist;
                            ciudadDestino = destino;
                        }
                    } else break;
                }
            }
            // hago la elección y vuelvo a iterar
            ciudadesVisitadas.add(ciudadDestino);
            Algoritmos.distanciaTotal += minDistance;
            rutaAristas.add(new Arista(ciudadActual, ciudadDestino, minDistance));
            ciudadActual = ciudadDestino;
        }

        // volvemos al inicio
        double distanciaVuelta = distancia(ciudadesVisitadas.getLast(), ciudadesVisitadas.getFirst());
        Algoritmos.distanciaTotal += distanciaVuelta;
        rutaAristas.add(new Arista(ciudadesVisitadas.getLast(), ciudadesVisitadas.getFirst(), distanciaVuelta));

        double end = System.nanoTime();
        return new Resultado("Poda Unidireccional", Algoritmos.getDistanciaTotal(), Algoritmos.getCalculadas(), (end - start) / 1e6, rutaAristas);
    }


    public static Resultado exhaustivoBidireccional(ArrayList<Punto> ciudades) {
        Algoritmos.setParameters();
        double start = System.nanoTime();

        ArrayList<Punto> ciudadesVisitadas = new ArrayList<>();

        // extremos
        ArrayList<Punto> extremoDer = new ArrayList<>();
        ArrayList<Punto> extremoIzq = new ArrayList<>();
        ArrayList<Arista> extremoAristaIzq = new ArrayList<>();
        ArrayList<Arista> extremoAristaDer = new ArrayList<>();

        extremoIzq.add(ciudades.get(Algoritmos.getCiudadPartida()));
        extremoDer.add(ciudades.get(Algoritmos.getCiudadPartida()));
        ciudadesVisitadas.add(ciudades.get(Algoritmos.getCiudadPartida()));

        Punto ciudadActualIzq = ciudades.get(Algoritmos.getCiudadPartida());
        Punto ciudadActualDer = ciudades.get(Algoritmos.getCiudadPartida());

        Algoritmos.quickSort(ciudades, 'x');

        while (ciudadesVisitadas.size() < ciudades.size()) {
            // buscamos unidireccionalmente y lo dejamos en extremo de la derecna
            double minDistanceDer = Double.MAX_VALUE, minDistanceIzq = Double.MAX_VALUE;
            Punto destinoDer = null, destinoIzq = null;

            for (Punto destino : ciudades) {
                if (ciudadesVisitadas.size() == 1 && !ciudadesVisitadas.contains(destino)) {
                    // lo queremos agregar en el extremo de la derecha
                    double dist = distancia(ciudadActualDer, destino);
                    if (dist < minDistanceDer) {
                        minDistanceDer = dist;
                        destinoDer = destino;
                    } else break;
                } else {
                    // comienza la búsqueda bidireccional
                    if (!ciudadesVisitadas.contains(destino)) {
                        // calculamos derecha
                        double dist = distancia(ciudadActualIzq, destino);
                        if (dist < minDistanceIzq) {
                            minDistanceIzq = dist;
                            destinoIzq = destino;
                        }

                        // calculamos der
                        dist = distancia(ciudadActualDer, destino);
                        if (dist < minDistanceDer) {
                            minDistanceDer = dist;
                            destinoDer = destino;
                        }
                    }
                }
            }

            // hacemos la selecion
            if (minDistanceIzq <= minDistanceDer) {
                // nos quedams con el punto de la izquierdo
                extremoAristaIzq.add(new Arista(ciudadActualIzq, destinoIzq, minDistanceIzq));
                ciudadActualIzq = destinoIzq;
                extremoIzq.add(destinoIzq);
                distanciaTotal += minDistanceIzq;
                ciudadesVisitadas.add(destinoIzq);
            } else {
                extremoAristaDer.add(new Arista(destinoDer, ciudadActualDer, minDistanceDer));
                ciudadActualDer = destinoDer;
                extremoDer.add(destinoDer);
                distanciaTotal += minDistanceDer;
                ciudadesVisitadas.add(destinoDer);
            }
        }

        // calculamos la distancia que nos quda por detrás
        double d = distancia(extremoIzq.getLast(), extremoDer.getLast());
        distanciaTotal += d;
        extremoAristaIzq.add(new Arista(extremoIzq.getLast(), extremoDer.getLast(), d));

        // MONTAR LA RUTA
        ArrayList<Arista> rutaAristas = new ArrayList<>(extremoAristaIzq);
        for (int i = extremoAristaDer.size() - 1; i >= 0; i--) {
            rutaAristas.add(extremoAristaDer.get(i));
        }

        double end = System.nanoTime();
        return new Resultado("Exhaustivo Bidirecional", Algoritmos.getDistanciaTotal(), Algoritmos.getCalculadas(), (end - start) / 1e6, rutaAristas);
    }


    public static Resultado podaBidireccional(ArrayList<Punto> ciudades) {
        Algoritmos.setParameters();
        double start = System.nanoTime();

        ArrayList<Punto> ciudadesVisitadas = new ArrayList<>();

        // extremos
        ArrayList<Punto> extremoDer = new ArrayList<>();
        ArrayList<Punto> extremoIzq = new ArrayList<>();
        ArrayList<Arista> extremoAristaIzq = new ArrayList<>();
        ArrayList<Arista> extremoAristaDer = new ArrayList<>();

        extremoIzq.add(ciudades.get(Algoritmos.getCiudadPartida()));
        extremoDer.add(ciudades.get(Algoritmos.getCiudadPartida()));
        ciudadesVisitadas.add(ciudades.get(Algoritmos.getCiudadPartida()));

        Punto ciudadActualIzq = ciudades.get(Algoritmos.getCiudadPartida());
        Punto ciudadActualDer = ciudades.get(Algoritmos.getCiudadPartida());

        while (ciudadesVisitadas.size() < ciudades.size()) {
            // buscamos unidireccionalmente y lo dejamos en extremo de la derecna
            double minDistanceDer = Double.MAX_VALUE, minDistanceIzq = Double.MAX_VALUE;
            Punto destinoDer = null, destinoIzq = null;

            for (Punto destino : ciudades) {
                if (ciudadesVisitadas.size() == 1 && !ciudadesVisitadas.contains(destino)) {
                    // lo queremos agregar en el extremo de la derecha
                    double dist = distancia(ciudadActualDer, destino);
                    if (dist < minDistanceDer) {
                        minDistanceDer = dist;
                        destinoDer = destino;
                    }
                } else {
                    // comienza la búsqueda bidireccional
                    if (!ciudadesVisitadas.contains(destino)) {
                        double dist = 0;
                        // calculamos derecha
                        if (Math.abs(ciudadActualIzq.getX() - destino.getX()) < minDistanceIzq) {
                            dist = distancia(ciudadActualIzq, destino);
                            if (dist < minDistanceIzq) {
                                minDistanceIzq = dist;
                                destinoIzq = destino;
                            }
                        }

                        // calculamos der
                        if (Math.abs(ciudadActualDer.getX() - destino.getX()) < minDistanceDer) {
                            dist = distancia(ciudadActualDer, destino);
                            if (dist < minDistanceDer) {
                                minDistanceDer = dist;
                                destinoDer = destino;
                            }
                        }
                    }
                }
            }

            // hacemos la selecion
            if (minDistanceIzq <= minDistanceDer) {
                // nos quedams con el punto de la izquierdo
                extremoAristaIzq.add(new Arista(ciudadActualIzq, destinoIzq, minDistanceIzq));
                ciudadActualIzq = destinoIzq;
                extremoIzq.add(destinoIzq);
                distanciaTotal += minDistanceIzq;
                ciudadesVisitadas.add(destinoIzq);
            } else {
                extremoAristaDer.add(new Arista(destinoDer, ciudadActualDer, minDistanceDer));
                ciudadActualDer = destinoDer;
                extremoDer.add(destinoDer);
                distanciaTotal += minDistanceDer;
                ciudadesVisitadas.add(destinoDer);
            }
        }

        // calculamos la distancia que nos quda por detrás
        distanciaTotal += distancia(extremoIzq.getLast(), extremoDer.getLast());

        // MONTAR LA RUTA
        ArrayList<Arista> rutaAristas = new ArrayList<>(extremoAristaIzq);
        for (int i = extremoAristaDer.size() - 1; i >= 0; i--) {
            rutaAristas.add(extremoAristaDer.get(i));
        }

        double end = System.nanoTime();
        return new Resultado("Poda Bidirecional", Algoritmos.getDistanciaTotal(), Algoritmos.getCalculadas(), (end - start) / 1e6, rutaAristas);
    }

    /* ----- ORDENACION ----- */
    public static void quickSort(ArrayList<Punto> t, char eje) {
        quickSort(t, 0, t.size() - 1, eje);
    }

    public static void quickSort(ArrayList<Punto> t, int izq, int der, char eje) {
        int q = 0;

        switch (eje) {
            case 'x' -> {
                if (izq < der) {
                    q = partition(t, izq, der, 'x');
                    quickSort(t, izq, q - 1, 'x');
                    quickSort(t, q + 1, der, 'x');
                }
            }

            case 'y' -> {
                if (izq < der) {
                    q = partition(t, izq, der, 'y');
                    quickSort(t, izq, q - 1, 'y');
                    quickSort(t, q + 1, der, 'y');
                }
            }
        }
    }

    public static int partition(ArrayList<Punto> t, int izq, int der, char eje) {
        Punto pivote = t.get(der);
        int limIzq = izq - 1;

        switch (eje) {
            case 'x' -> {
                for (int j = izq; j < der; j++) {
                    if (t.get(j).getX() <= pivote.getX()) {
                        limIzq++;
                        // Intercambiamos t[limIzq] con t[j]
                        Punto aux = t.get(limIzq);
                        t.set(limIzq, t.get(j));
                        t.set(j, aux);
                    }
                }
            }
            case 'y' -> {
                for (int j = izq; j < der; j++) {
                    if (t.get(j).getY() <= pivote.getY()) {
                        limIzq++;
                        // Intercambiamos t[limIzq] con t[j]
                        Punto aux = t.get(limIzq);
                        t.set(limIzq, t.get(j));
                        t.set(j, aux);
                    }
                }
            }
        }
        // Colocamos el pivote en su posición correcta
        Punto aux = t.get(limIzq + 1);
        t.set(limIzq + 1, t.get(der));
        t.set(der, aux);

        return limIzq + 1;  // Retornamos la posición del pivote
    }

}
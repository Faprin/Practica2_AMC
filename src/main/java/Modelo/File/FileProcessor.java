package Modelo.File;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;

import Modelo.Algoritmos.Arista;
import Modelo.Algoritmos.Punto;
import javafx.scene.control.Alert;

public class FileProcessor {

    public static ArrayList<Punto> reader(String path) {
        ArrayList<Punto> puntos = new ArrayList<>();

        try (FileReader r = new FileReader(path);
             BufferedReader b = new BufferedReader(r)) {
            // me salto las 3 primeras lineas y leemos la dimensión
            for (int i = 0; i < 3; i++) {
                b.readLine();
            }

            // lee la dimensión
            String dimensionLine = b.readLine();
            String[] dimensionWOFormat = dimensionLine.split(": ");
            int dimension = Integer.parseInt(dimensionWOFormat[1]);

            // saltamos las dos lineas siguientes
            for (int i = 0; i < 2; i++) {
                b.readLine();
            }

            for (int i = 0; i < dimension; i++) {
                String pointLine = b.readLine();
                String[] pointWOFormat = pointLine.split(" ");
                Punto p = new Punto();
                p.setId(Integer.parseInt(pointWOFormat[0]));
                p.setX(Double.parseDouble(pointWOFormat[1]));
                p.setY(Double.parseDouble(pointWOFormat[2]));
                puntos.add(p);
            }

        } catch (Exception e) {
            System.out.println("ERROR a la hora  de leer el archivo -> " + e.getMessage());
        }

        return puntos;
    }

    public static void createDataset(int dimension) {
        try {
            // definicion del nombre del archivo
            Random numRandom = new Random(System.nanoTime());
            String nomFichero = "datasets/dataset" + dimension + ".tsp";
            File ficheroAleatorio = new File(nomFichero);

            // creamos el fileWriter y metemos el nombre
            FileWriter fichero = new FileWriter(nomFichero);
            fichero.write("NAME: " + nomFichero + "\n");

            // agrego el resto de cabeceras para que mantenga el mismo formato
            fichero.write("TYPE: TSP \nCOMMENT: " + dimension + "\nDIMENSION: " + dimension +
                    "\nEDGE_WEIGHT_TYPE: NAN\nNODE_COORD_SECTION\n");

            // iremos escribiendo en el fichero los datos que generemos aleatoriamente
            for (int i = 0; i < dimension; i++) {
                double x = numRandom.nextDouble(500 - 0 + 1);
                double y = numRandom.nextDouble(500 - 0 + 1);
                fichero.write(i + 1 + " " + Math.round(x * 1e10) / 1e10 + " " + Math.round(y * 1e10) / 1e10 + "\n");
            }

            // indicamos el final del fichero
            fichero.write("EOF");
            fichero.close();
            System.out.println("Fichero creado satisfactoriamente");

        } catch (Exception e) {
            System.out.println("No se ha podido crear el nuevo fichero: " + e.getMessage());
        }
    }

    public static void createAlgorithmFile(String algoritmo, ArrayList<Arista> aristas, double distancia, boolean esUnidireccional) {
        // elegimos el nombre del fichero


        try {
            String nomFichero = "ficherosEstrategias/" + algoritmo + ".dat";
            FileWriter fichero = new FileWriter(nomFichero);

            // escribimos las primras lineas
            fichero.write("NAME: " + nomFichero + "\n");
            fichero.write("TYPE: NULL\n");
            fichero.write("DIMENSION: " + aristas.size() + "\n");
            fichero.write("SOLUTION: " + distancia + "\n\n");

            for (Arista arista : aristas) {
                fichero.write(arista.getOrigen().getId() + ", ");
                if (aristas.getLast().equals(arista)) {
                    fichero.write("" + arista.getDestino().getId());
                }
            }

            fichero.write("\n\n");
            for (Arista arista : aristas) {
                fichero.write((double) Math.round(arista.getDistancia() * 1000) / 1000 + " - " + arista.getOrigen() + ", " + arista.getDestino() + "\n");
            }

            fichero.write("EOF");
            fichero.close();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No se ha podido crear el nuevo fichero");
            alert.setContentText("No se ha podido crear el nuevo fichero: " + e.getMessage());
            alert.showAndWait();
        }

    }
}

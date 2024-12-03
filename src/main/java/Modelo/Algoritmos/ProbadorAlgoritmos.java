package Modelo.Algoritmos;

import java.util.ArrayList;

import Controlador.Resultado;
import Modelo.File.FileProcessor;

public class ProbadorAlgoritmos {

    public static double calculaDistanciaPorArista(ArrayList<Arista> aristas) {
        double retorno = 0;
        for (Arista arista : aristas) {
            retorno += arista.getDistancia();
        }

        return retorno;
    }

    public static void main(String[] args) {
        //String path = "datasets/dataset5.tsp";
        String path = "datasets/ch130.tsp";
        ArrayList<Punto> ciudades = FileProcessor.reader(path);

        Algoritmos.setCiudadPartida(ciudades.size());

        // COMPROBACION DEL VORAZ EXHAUSTIVO UNIDIRECCIONAL
        Resultado r1 = Algoritmos.exhaustivoUnidireccional(ciudades);
        System.out.println("Exhaustiva unidireccional -> " + Algoritmos.getDistanciaTotal() + ", calculadas -> " +
                Algoritmos.getCalculadas());

        // COMPROBACION DEL VORAZ EXHAUSTIV BIDIRECCIONAL
        Algoritmos.exhaustivoBidireccional(ciudades);
        System.out.println("Exhaustiva bidireccional -> " + Algoritmos.getDistanciaTotal() + ", calculadas -> " +
                Algoritmos.getCalculadas());


        // COMPROBACION DEL VORAZ PODA UNIDIRECCIONAL
        r1 = Algoritmos.podaUnidireccional(ciudades); 
        System.out.println("Poda Unidireccional -> " + Algoritmos.getDistanciaTotal() + ", calculadas -> " +
                Algoritmos.getCalculadas() + ", tiempo: " + r1.getTiempo());

        // COMPROBACION DEL VORAZ PODA BIDIRECCIONAL
        Algoritmos.podaBidireccional(ciudades);
        System.out.println("Poda Bidireccional aRISTA -> " + Algoritmos.getDistanciaTotal() + ", calculadas -> " +
                Algoritmos.getCalculadas());



    }
}

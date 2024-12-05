package Controlador;

import Modelo.Algoritmos.Algoritmos;
import Modelo.Algoritmos.Punto;
import Modelo.File.FileProcessor;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ControladorResultadosCompararEstrategias implements Initializable {

    @FXML
    private TableColumn<Resultado, Integer> columnaCalculadas;

    @FXML
    private TableColumn<Resultado, Double> columnaDistancia;

    @FXML
    private TableColumn<Resultado, String> columnaNombre;

    @FXML
    private TableColumn<Resultado, Double> columnaTiempo;

    @FXML
    private TableView<Resultado> tablaResultados;

    private static ArrayList<Punto> puntos = new ArrayList<>();

    private final ObservableList<Resultado> resultados = FXCollections.observableList(new ArrayList<>());

    public static void setPuntos(ArrayList puntos) {
        ControladorResultadosCompararEstrategias.puntos.clear();
        ControladorResultadosCompararEstrategias.puntos.addAll(puntos);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        columnaCalculadas.setCellValueFactory(stablerResultant -> new SimpleObjectProperty<>(stablerResultant.getValue().getCalculadas()));
        columnaDistancia.setCellValueFactory(stablerResultant -> new SimpleObjectProperty<>(stablerResultant.getValue().getDistancia()));
        columnaNombre.setCellValueFactory(stablerResultant -> new SimpleObjectProperty<>(stablerResultant.getValue().getNombre()));
        columnaTiempo.setCellValueFactory(stablerResultant -> new SimpleObjectProperty<>(stablerResultant.getValue().getTiempo()));

        resultados.clear();

        Algoritmos.setCiudadPartida(puntos.size());
        Resultado r1 = Algoritmos.exhaustivoUnidireccional(new ArrayList<Punto>(puntos));
        resultados.add(r1);
        FileProcessor.createAlgorithmFile("exhaustivoUnidireccional", r1.getAristas(), Algoritmos.getDistanciaTotal(), true);

        r1 = Algoritmos.exhaustivoBidireccional(new ArrayList<>(puntos));
        resultados.add(r1);
        FileProcessor.createAlgorithmFile("exhaustivoBidireccional", r1.getAristas(), Algoritmos.getDistanciaTotal(), false);

        r1 = Algoritmos.podaUnidireccional(new ArrayList<>(puntos));
        resultados.add(r1);
        FileProcessor.createAlgorithmFile("podaUnidireccional", r1.getAristas(), Algoritmos.getDistanciaTotal(), true);

        r1 = Algoritmos.podaBidireccional(new ArrayList<>(puntos));
        resultados.add(r1);
        FileProcessor.createAlgorithmFile("podaBidireccional", r1.getAristas(), Algoritmos.getDistanciaTotal(), false);


        tablaResultados.setItems(resultados);
    }
}

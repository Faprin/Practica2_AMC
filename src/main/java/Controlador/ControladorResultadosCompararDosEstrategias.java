package Controlador;

import Modelo.Algoritmos.Algoritmos;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import Modelo.Algoritmos.Punto;
import javafx.scene.control.TableView;

public class ControladorResultadosCompararDosEstrategias implements Initializable {

    @FXML
    private TableColumn<ResultadoDos, Integer> columnaCalculadas1;

    @FXML
    private TableColumn<ResultadoDos, Integer> columnaCalculadas2;

    @FXML
    private TableColumn<ResultadoDos, Integer> columnaTalla;

    @FXML
    private TableColumn<ResultadoDos, Double> columnaTiempo1;

    @FXML
    private TableColumn<ResultadoDos, Double> columnaTiempo2;

    @FXML
    private CheckBox exhaustivoBidireccional;

    @FXML
    private CheckBox exhaustivoUnidireccional;

    @FXML
    private CheckBox podaBidireccional;

    @FXML
    private CheckBox podaUnidireccional;


    @FXML
    private TableView<ResultadoDos> tablaResultados;

    private final ObservableList<ResultadoDos> resultados = FXCollections.observableList(new ArrayList<>());

    private static ArrayList<Punto> puntos = new ArrayList<>();

    public static void setPuntos(ArrayList puntos) {
        ControladorResultadosCompararDosEstrategias.puntos.clear();
        ControladorResultadosCompararDosEstrategias.puntos.addAll(puntos);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tablaResultados.setItems(resultados);

        columnaTalla.setCellValueFactory(stableResultant -> new SimpleObjectProperty<>(stableResultant.getValue().getTalla()));
        columnaCalculadas1.setCellValueFactory(stableResultant -> new SimpleObjectProperty<>(stableResultant.getValue().getCalculadas1()));
        columnaCalculadas2.setCellValueFactory(stableResultant -> new SimpleObjectProperty<>(stableResultant.getValue().getCalculadas2()));
        columnaTiempo1.setCellValueFactory(stableResultant -> new SimpleObjectProperty<>(stableResultant.getValue().getTiempo1()));
        columnaTiempo2.setCellValueFactory(stableResultant -> new SimpleObjectProperty<>(stableResultant.getValue().getTiempo2()));
        // Algoritmos.setCiudadPartida(puntos.size());
    }

    public void comparar() {
        ArrayList<String> algoritmos = new ArrayList<>();
        resultados.clear();

        if (exhaustivoUnidireccional.isSelected()) {
            algoritmos.add("Exhaustivo Unidireccional");
        }
        if (exhaustivoBidireccional.isSelected()) {
            algoritmos.add("Exhaustivo Bidireccional");
        }
        if (podaUnidireccional.isSelected()) {
            algoritmos.add("Poda Unidireccional");
        }
        if (podaBidireccional.isSelected()) {
            algoritmos.add("Poda Bidireccional");
        }

        try {
            if (algoritmos.size() != 2) {
                throw new Exception("Por favor, seleccione exactamente dos algoritmos");
            }

            columnaCalculadas1.setText(algoritmos.get(0) + " calculadas");
            columnaCalculadas2.setText(algoritmos.get(1) + " calculadas");
            columnaTiempo1.setText(algoritmos.get(0) + " tiempo");
            columnaTiempo2.setText(algoritmos.get(1) + " tiempo");

            double acumuladorTiempoAlg1 = 0;
            double acumuladorTiempoAlg2 = 0;
            int acumuladorCalculadasAlg1 = 0;
            int acumuladorCalculadasAlg2 = 0;

            for (int i = 100; i < 500; i += 100) {
                // para cada talla, 10 datasets distintos de dicha talla
                for (int j = 0; j < 10; j++) {
                    ArrayList<Punto> dataset = ControladorMenuPrincipal.generaDatasetPorTalla(i);

                    for (int k = 0; k < algoritmos.size(); k++) {
                        switch (algoritmos.get(k)) {
                            case "Exhaustivo Unidireccional" -> {
                                if (k == 0) {
                                    Resultado r = Algoritmos.exhaustivoUnidireccional(new ArrayList<>(dataset));
                                    acumuladorCalculadasAlg1 += r.getCalculadas();
                                    acumuladorTiempoAlg1 += r.getTiempo();
                                } else {
                                    Resultado r = Algoritmos.exhaustivoUnidireccional(new ArrayList<>(dataset));
                                    acumuladorCalculadasAlg2 += r.getCalculadas();
                                    acumuladorTiempoAlg2 += r.getTiempo();
                                }
                            }
                            case "Exhaustivo Bidireccional" -> {
                                if (k == 0) {
                                    Resultado r = Algoritmos.exhaustivoBidireccional(new ArrayList<>(dataset));
                                    acumuladorCalculadasAlg1 += r.getCalculadas();
                                    acumuladorTiempoAlg1 += r.getTiempo();
                                } else {
                                    Resultado r = Algoritmos.exhaustivoBidireccional(new ArrayList<>(dataset));
                                    acumuladorCalculadasAlg2 += r.getCalculadas();
                                    acumuladorTiempoAlg2 += r.getTiempo();
                                }
                            }
                            case "Poda Unidireccional" -> {
                                if (k == 0) {
                                    Resultado r = Algoritmos.podaUnidireccional(new ArrayList<>(dataset));
                                    acumuladorCalculadasAlg1 += r.getCalculadas();
                                    acumuladorTiempoAlg1 += r.getTiempo();
                                } else {
                                    Resultado r = Algoritmos.podaUnidireccional(new ArrayList<>(dataset));
                                    acumuladorCalculadasAlg2 += r.getCalculadas();
                                    acumuladorTiempoAlg2 += r.getTiempo();
                                }
                            }
                            case "Poda Bidireccional" -> {
                                if (k == 0) {
                                    Resultado r = Algoritmos.podaBidireccional(new ArrayList<>(dataset));
                                    acumuladorCalculadasAlg1 += r.getCalculadas();
                                    acumuladorTiempoAlg1 += r.getTiempo();
                                } else {
                                    Resultado r = Algoritmos.podaBidireccional(new ArrayList<>(dataset));
                                    acumuladorCalculadasAlg2 += r.getCalculadas();
                                    acumuladorTiempoAlg2 += r.getTiempo();
                                }
                            }
                        }
                    }
                }

                // agregamos los resultados a resulgado
                resultados.add(new ResultadoDos(i, (double) Math.round(acumuladorTiempoAlg1 * 1000) / 1000,
                        (double) Math.round(acumuladorTiempoAlg2*1000) / 1000,
                        acumuladorCalculadasAlg1, acumuladorCalculadasAlg2));

            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Seleccione exactamente dos algoritmos");
            alert.setContentText("Seleccione exactamente dos algoritmos para comparar");
            alert.showAndWait();
        }
    }
}



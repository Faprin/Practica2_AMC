package Controlador;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import Modelo.Algoritmos.Algoritmos;
import Modelo.Algoritmos.Punto;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

public class ControladorComprobarEstrategias implements Initializable {

    public class resultadoTiempos {
        private double exhaustivoUnidireccional = 0,
                podaUnidireccional = 0,
                exhaustivoBidireccional = 0,
                podaBidireccional = 0;

        private int talla;

        public resultadoTiempos(double exhaustivoUnidireccional, double podaUnidireccional, double exhaustivoBidireccional, double podaBidireccional, int talla) {
            this.exhaustivoUnidireccional = exhaustivoUnidireccional;
            this.podaUnidireccional = podaUnidireccional;
            this.exhaustivoBidireccional = exhaustivoBidireccional;
            this.podaBidireccional = podaBidireccional;
            this.talla = talla;
        }

        public double getExhaustivoUnidireccional() {
            return exhaustivoUnidireccional;
        }

        public void setExhaustivoUnidireccional(double exhaustivoUnidireccional) {
            this.exhaustivoUnidireccional = exhaustivoUnidireccional;
        }

        public double getPodaUnidireccional() {
            return podaUnidireccional;
        }

        public void setPodaUnidireccional(double podaUnidireccional) {
            this.podaUnidireccional = podaUnidireccional;
        }

        public double getExhaustivoBidireccional() {
            return exhaustivoBidireccional;
        }

        public void setExhaustivoBidireccional(double exhaustivoBidireccional) {
            this.exhaustivoBidireccional = exhaustivoBidireccional;
        }

        public double getPodaBidireccional() {
            return podaBidireccional;
        }

        public void setPodaBidireccional(double podaBidireccional) {
            this.podaBidireccional = podaBidireccional;
        }

        public int getTalla() {
            return talla;
        }

        public void setTalla(int talla) {
            this.talla = talla;
        }
    }

    ObservableList<resultadoTiempos> tiempos = FXCollections.observableList(new ArrayList<>());

    @FXML
    private TableColumn<resultadoTiempos, Double> exhaustivoBidireccional;

    @FXML
    private TableColumn<resultadoTiempos, Double> exhaustivoUnidireccional;

    @FXML
    private TableColumn<resultadoTiempos, Double> podaBidireccional;

    @FXML
    private TableColumn<resultadoTiempos, Double> podaUnidireccional;

    @FXML
    private TableColumn<resultadoTiempos, Integer> talla;

    @FXML
    private TableView<resultadoTiempos> tabla;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        talla.setCellValueFactory(stableResultant -> new SimpleObjectProperty<>(stableResultant.getValue().getTalla()));
        exhaustivoUnidireccional.setCellValueFactory(stableResultant -> new SimpleObjectProperty<>(stableResultant.getValue().getExhaustivoUnidireccional()));
        podaUnidireccional.setCellValueFactory(stableResultant -> new SimpleObjectProperty<>(stableResultant.getValue().getPodaUnidireccional()));
        exhaustivoBidireccional.setCellValueFactory(stableResultant -> new SimpleObjectProperty<>(stableResultant.getValue().getExhaustivoBidireccional()));
        podaBidireccional.setCellValueFactory(stableResultant -> new SimpleObjectProperty<>(stableResultant.getValue().getPodaBidireccional()));

        tiempos.clear();
        Resultado resultado;
        for (int i = 100; i <= 500; i += 100) {
            double exhaustivoUnidireccional = 0,
                    podaUnidireccional = 0,
                    exhaustivoBidireccional = 0,
                    podaBidireccional = 0;
            for (int j = 0; j < 10; j++) {
                ArrayList<Punto> dataset = ControladorMenuPrincipal.generaDatasetPorTalla(i);
                // ejecutar todos los algoritmos y tomar tiempos
                Algoritmos.setCiudadPartida(dataset.size());
                resultado = Algoritmos.exhaustivoUnidireccional(new ArrayList<>(dataset));
                exhaustivoUnidireccional += resultado.getTiempo();

                resultado = Algoritmos.podaUnidireccional(new ArrayList<>(dataset));
                podaUnidireccional += resultado.getTiempo();

                resultado = Algoritmos.exhaustivoBidireccional(new ArrayList<>(dataset));
                exhaustivoBidireccional += resultado.getTiempo();

                resultado = Algoritmos.podaBidireccional(new ArrayList<>(dataset));
                podaBidireccional += resultado.getTiempo();
            }

            tiempos.add(new resultadoTiempos((double) Math.round((exhaustivoUnidireccional * 1000) / 1000) /4,
                    (double) Math.round((podaUnidireccional * 1000) / 1000) /4,
                    (double) Math.round((exhaustivoBidireccional * 1000) / 1000) /4,
                    (double) Math.round((podaBidireccional * 1000) / 1000) /4,
                    i));
        }

        tabla.setItems(tiempos);

    }


}

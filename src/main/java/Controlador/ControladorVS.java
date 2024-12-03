package Controlador;

import Modelo.Algoritmos.Algoritmos;
import Modelo.Algoritmos.Punto;
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

public class ControladorVS implements Initializable {

    @FXML
    private TableColumn<ResultadoVersus, Double> tBidireccional;

    @FXML
    private TableColumn<ResultadoVersus, Double> tUnidireccional;

    @FXML
    private TableColumn<ResultadoVersus, Integer> talla;

    @FXML
    private TableColumn<ResultadoVersus, String> vencedor;

    @FXML
    private TableView<ResultadoVersus> tabla;

    ObservableList<ResultadoVersus> resultado = FXCollections.observableList(new ArrayList<>());

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // puedo setear los resultados desde aquí es decir, hago las operaciones y relleno el observable
        talla.setCellValueFactory(stableResultant -> new SimpleObjectProperty<>(stableResultant.getValue().getTalla()));
        vencedor.setCellValueFactory(stableResultant -> new SimpleObjectProperty<>(stableResultant.getValue().getVencedor()));
        tUnidireccional.setCellValueFactory(stableResultant -> new SimpleObjectProperty<>(stableResultant.getValue().getTiempoUnidireccional()));
        tBidireccional.setCellValueFactory(stableResultant -> new SimpleObjectProperty<>(stableResultant.getValue().getTiempoBidireccional()));

        resultado.clear();

        Resultado resultadoUnidireccional, resultadoBidireccional;
        int contadorBidireccional = 0, contadorUnidireccional = 0;
        // caulculo el valor
        for (int i = 100; i <= 500; i += 100) {
            double tiempoUnidireccional = 0, tiempoBidireccional = 0;
            double distanciaUnidireccional = 0, distanciaBidireccional = 0;
            contadorUnidireccional = 0;
            contadorBidireccional = 0;
            for (int j = 0; j < 11; j++) {
                ArrayList<Punto> dataset = ControladorMenuPrincipal.generaDatasetPorTalla(i);

                // aplico una busqueda unidirecdcional
                resultadoUnidireccional = Algoritmos.podaUnidireccional(new ArrayList<>(dataset));
                tiempoUnidireccional += resultadoUnidireccional.getTiempo();
                distanciaUnidireccional += resultadoUnidireccional.getDistancia();
                // aplico una busqueda bidireccional
                resultadoBidireccional = Algoritmos.podaBidireccional(new ArrayList<>(dataset));
                tiempoBidireccional += resultadoBidireccional.getTiempo();
                distanciaBidireccional += resultadoBidireccional.getDistancia();

                if(resultadoUnidireccional.getDistancia() <= resultadoBidireccional.getDistancia()) {
                    contadorUnidireccional++;
                } else {
                    contadorBidireccional++;
                }
            }

            // miro quien gana en cada iteracio
            // agrego a la tabla el restulado ->
            String vencedor = " ";
            if(contadorUnidireccional > contadorBidireccional){
                vencedor = "Unidireccional -> " + contadorUnidireccional;
            } else {
                vencedor = "Bidireccional -> " + contadorBidireccional;
            }

            resultado.add(new ResultadoVersus(i, vencedor, (double) Math.round(tiempoUnidireccional * 1000) / 1000,
                    (double) Math.round(tiempoBidireccional * 1000) / 1000 ));
        }

        tabla.setItems(resultado);
    }
}

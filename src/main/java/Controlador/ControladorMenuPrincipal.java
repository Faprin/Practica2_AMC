package Controlador;

import Modelo.Algoritmos.Algoritmos;
import Modelo.Algoritmos.Arista;
import Modelo.File.FileProcessor;
import javafx.application.Application;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;

import Modelo.Algoritmos.Punto;

public class ControladorMenuPrincipal extends Application {

    private final double radio = 2;

    private static Color color = Color.BLACK;

    private static String algoritmoVisualizar = null;

    private static final ObservableList<Punto> puntos = FXCollections.observableList(new ArrayList<>());

    public static void loadFile(String path) {
        puntos.setAll(FileProcessor.reader(path));
    }

    public static int iterator = 0;

    private void inicializarListeners() {
        puntos.addListener(new ListChangeListener<Punto>() {
            @Override
            public void onChanged(Change<? extends Punto> change) { // el observable va a llamar a esto cada vez que cambie
                repaintPuntos();
            }
        });

        resultado.addListener(new ChangeListener<Resultado>() {
            @Override
            public void changed(ObservableValue<? extends Resultado> observableValue, Resultado resultado, Resultado t1) {
                repaintSolucion();
            }
        });
    }

    public ControladorMenuPrincipal() {
        inicializarListeners();
    }

    public static ArrayList<Punto> generaDatasetPorTalla(int talla) {
        // tenemos que verificar si la variable peorCaso esta a true
        Random random = new Random(System.nanoTime());
        ArrayList<Punto> datos = new ArrayList<>();
        int num = 0, den = 0;
        double x, y;
        for (int i = 0; i < talla; i++) {
            num = random.nextInt(4000 - 1 + 1) + 1; // entre 1 y 4000
            den = random.nextInt(17 - 7 + 1) + 7; // entre 1 y 17
            x = num / ((double) den + 0.37);
            y = (random.nextInt(4000 - 1 + 1) + 1) / ((double) (random.nextInt(17 - 7 + 1) + 7) + 0.37);
            datos.add(new Punto());
            datos.get(i).setId(i + 1);
            datos.get(i).setX(x);
            datos.get(i).setY(y);
        }

        return datos;
    }

    @FXML
    private Button botonSalir;

    @FXML
    private AnchorPane contenedorPuntos;

    @FXML
    private ComboBox<String> selectorPuntoPartida;

    @FXML
    ComboBox<String> selectorTrazaAlgoritmo;

    private int inicio; // Variable para guardar el valor ingresado

    @FXML
    public void initialize() {

        contenedorPuntos.getChildren().clear();
        Text t1 = new Text(50, 50, "PRÁCTICA 2 AMC. Jose Manuel Domínguez Segura");
        t1.setFont(new Font(23));
        t1.setFill(Color.BLACK);

        Text t2 = new Text(50,90, "-> Cargue un dataset en memoria para visualizar los puntos en esta pantalla");
        t2.setFont(new Font(15));
        contenedorPuntos.getChildren().addAll(t1, t2);

        selectorPuntoPartida.setItems(FXCollections.observableArrayList("Auto", "Seleccionar..."));

        // COMBOBOX SELECTOR DE PUNTO DE PARTIDA
        selectorPuntoPartida.setOnAction(event -> {
            String selected = selectorPuntoPartida.getValue();
            if ("Seleccionar...".equals(selected)) {
                // Crear un cuadro de diálogo para el input del usuario
                TextInputDialog dialog = new TextInputDialog();
                dialog.setTitle("Seleccionar punto de partida");
                dialog.setHeaderText("Introduce el número del punto de partida:");
                dialog.setContentText("Número:");

                // Mostrar el diálogo y capturar la entrada
                Optional<String> result = dialog.showAndWait();
                result.ifPresentOrElse(input -> {
                    try {
                        inicio = Integer.parseInt(input);
                        if (inicio < 0 || inicio > puntos.size()) {
                            throw new NumberFormatException();
                        }

                        System.out.println("Número seleccionado: " + inicio); // Verificación en consola
                        Algoritmos.setPuntoPartida(inicio);

                        String nuevoValor = "Inicio: " + inicio;
                        if (!selectorPuntoPartida.getItems().contains(nuevoValor)) {
                            selectorPuntoPartida.getItems().add(nuevoValor);
                        }
                        selectorPuntoPartida.setValue(nuevoValor);
                    } catch (NumberFormatException e) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Entrada no válida");
                        alert.setHeaderText("Por favor, introduce un número válido");
                        alert.setContentText("El valor ingresado no es un número válido.");
                        alert.showAndWait();
                        // Revertir la selección del ComboBox
                        selectorPuntoPartida.setValue("Auto");
                    }
                }, () -> {
                    selectorPuntoPartida.setValue("Auto");
                });
            } else if ("Auto".equals(selected)) {
                Algoritmos.setPuntoPartida(-1);
                selectorPuntoPartida.getItems().clear();
                selectorPuntoPartida.setItems(FXCollections.observableArrayList("Auto", "Seleccionar..."));
            }
        });

        // COMBOBOX SELECTOR DE RUTA DE ALGORITMO
        selectorTrazaAlgoritmo.setItems(FXCollections.observableArrayList("Exhaustivo Unidireccional",
                "Poda Unidireccional", "Exhaustivo Bidireccional", "Poda Bidireccional"));
        selectorTrazaAlgoritmo.setValue(null);

        selectorTrazaAlgoritmo.setOnAction(event -> {
            algoritmoVisualizar = selectorTrazaAlgoritmo.getValue();
        });

    }

    public void repaintPuntos() {
        if (contenedorPuntos != null) {
            contenedorPuntos.getChildren().clear();

            double ancho = contenedorPuntos.getWidth();
            double alto = contenedorPuntos.getHeight();

            Dimensionado dimensionado = new Dimensionado(ancho, alto, puntos);

            for (Punto p : puntos) {
                Punto redimensionado = dimensionado.redimensionar(p);
                contenedorPuntos.getChildren().add(new Circle(redimensionado.getX(), redimensionado.getY(), radio, Color.RED));
            }
        }
    }


    public void repaintSolucion() {
        if (contenedorPuntos != null) {
            repaintPuntos();

            double ancho = contenedorPuntos.getWidth();
            double alto = contenedorPuntos.getHeight();

            if (ancho == 0 || alto == 0) {
                return; // Evitar división por cero
            }

            Dimensionado dimensionado = new Dimensionado(ancho, alto, puntos);

            var puntos = resultado.get().getAristas();

            boolean primeraIter = true; // TODO -> Corregir esto porque no pinta correctamente el primer punto

            for (Arista a : puntos) {
                Punto origen = dimensionado.redimensionar(a.getOrigen());
                Punto destino = dimensionado.redimensionar(a.getDestino());

                if (primeraIter) {
                    Circle circle = new Circle(origen.getX(), origen.getY(), radio + 4, Color.GREEN);
                    contenedorPuntos.getChildren().add(circle);
                    primeraIter = false;
                }

                Line line = new Line(origen.getX(), origen.getY(), destino.getX(), destino.getY());
                line.setStroke(color);
                contenedorPuntos.getChildren().add(line);

                // Crear la flecha
                double tamanio = 8;
                double angulo = Math.atan2(destino.getY() - origen.getY(), destino.getX() - origen.getX());

                double puntoMedioX = (origen.getX() + destino.getX()) / 2;
                double puntoMedioY = (origen.getY() + destino.getY()) / 2;

                Text distancia = new Text(puntoMedioX, puntoMedioY, "" + (double) Math.round(a.getDistancia() * 100) / 100);
                distancia.setFill(Color.BLACK);
                contenedorPuntos.getChildren().add(distancia);

                // Coordenadas del triángulo de la flecha
                double x1 = destino.getX() - tamanio * Math.cos(angulo - Math.PI / 6);
                double y1 = destino.getY() - tamanio * Math.sin(angulo - Math.PI / 6);
                double x2 = destino.getX() - tamanio * Math.cos(angulo + Math.PI / 6);
                double y2 = destino.getY() - tamanio * Math.sin(angulo + Math.PI / 6);

                Polygon arrowHead = new Polygon();
                arrowHead.getPoints().addAll(destino.getX(), destino.getY(), x1, y1, x2, y2);
                arrowHead.setFill(Color.BLACK);

                contenedorPuntos.getChildren().add(arrowHead);
            }
        }
    }


    @FXML
    public void generarDatasetAleatorio() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Geneara Dataset");
        dialog.setHeaderText("Capacidad del dataset: ");
        Optional<String> result = dialog.showAndWait();

        result.ifPresent(capacidadStr -> {
            int capacidad = Integer.parseInt(capacidadStr);
            FileProcessor.createDataset(capacidad);

            // sacamos un dialogo que verifique al usuario que se ha creado el dataset
            Alert alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setTitle("Fichero creado satisfactoriamente");
            alerta.setHeaderText("El fichero se ha creado satisfactoriamente");
            alerta.showAndWait();
        });
    }

    @FXML
    public void cargarDatasetEnMemoria() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Cargar dataset en memoria");

        fileChooser.setInitialDirectory(new File("C:\\Users\\jmdom\\OneDrive - UNIVERSIDAD DE HUELVA\\Universidad\\3 AÑO\\ALGORITMICA Y MODELOS DE COMPUTACIÓN\\Prácticas\\PRACTICA2 FX\\Practica2\\datasets"));
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Fichero tsp", "*.tsp"));

        File ficheroSeleccionado = fileChooser.showOpenDialog(null);

        if (ficheroSeleccionado != null) {
            // obtengo la ruta que me ha indicado
            loadFile(ficheroSeleccionado.getAbsolutePath());

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Fichero exitoso");
            alert.setHeaderText("Fichero creado satisfactoriamente");
            alert.setContentText("Fichero importado satisfactoriamente");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Se ha producido un error en la seleeción del fichero");
            alert.setContentText("Se ha producido un error al intentar cargar un dataset al programa. Porfavor, asegurese de que está realizando correctamente la carga");
            alert.showAndWait();
        }

    }

    private final SimpleObjectProperty<Resultado> resultado = new SimpleObjectProperty<>();

    @FXML
    public void comprobarEstrategias() {

        try {
            if (algoritmoVisualizar == null) {
                throw new Exception("Por favor, seleccione un algoritmo para represetnar su traza");
            }

            Algoritmos.setCiudadPartida(puntos.size());

            // seleccion de la traza que el usuario desea visualizar
            Resultado pintar;
            switch (algoritmoVisualizar) {
                case "Exhaustivo Unidireccional" -> {
                    pintar = Algoritmos.exhaustivoUnidireccional(new ArrayList<>(puntos));
                    color = Color.BLUE;
                }
                case "Poda Unidireccional" -> {
                    pintar = Algoritmos.podaUnidireccional(new ArrayList<>(puntos));
                    color = Color.ORANGE;
                }
                case "Exhaustivo Bidireccional" -> {
                    pintar = Algoritmos.exhaustivoBidireccional(new ArrayList<>(puntos));
                    color = Color.PURPLE;
                }
                case "Poda Bidireccional" -> {
                    pintar = Algoritmos.podaBidireccional(new ArrayList<>(puntos));
                    color = Color.YELLOW;
                }
                default -> {
                    throw new Exception("Por favor, seleccione un algoritmo para represetnar su traza");
                }
            }
            resultado.set(pintar);

            ControladorResultadosCompararEstrategias.setPuntos(new ArrayList<>(puntos));
            if (puntos.isEmpty()) {
                throw new Exception("Es necesario cargar previamente un dataset en memoria");
            }
            FXMLLoader fxmlLoader = new FXMLLoader(ControladorMenuPrincipal.class.getResource("resultadosCompararEstrategias.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 700, 300);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(e.getMessage());
            alert.setContentText("Por favor, preste atención al mensaje de error y reintente operar");
            alert.showAndWait();
        }
    }


    @FXML
    public void comprobarDosEstrategias() {
        try {
            ControladorResultadosCompararDosEstrategias.setPuntos(new ArrayList<>(puntos));
            FXMLLoader fxmlLoader = new FXMLLoader(ControladorMenuPrincipal.class.getResource("resultadosCompararDosEstrategias.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 1400, 400);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void compararEstrategias() {
        // voy a subdelegarlo a la clase del controlador para mostrar las operaciones
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(ControladorMenuPrincipal.class.getResource("comprobarEstrategias.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 1050, 200);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            System.out.println("Fallo aqui ");
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void buttonVS() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(ControladorMenuPrincipal.class.getResource("unidireccionalVSBidireccional.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 700, 300);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void salir() {
        Stage stage = (Stage) botonSalir.getScene().getWindow();
        stage.close();

        System.exit(0);
    }


    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ControladorMenuPrincipal.class.getResource("MenuPrincipal.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 700);
        stage.setTitle("PRACTICA 2");

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}

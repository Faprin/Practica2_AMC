module com.example.Controlador {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;

    opens Controlador to javafx.fxml;

    exports Controlador;
}
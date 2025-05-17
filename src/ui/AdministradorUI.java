package ui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class AdministradorUI extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Panel Principal - Administrador");

        Label lblTitulo = new Label("Panel Principal - Administrador");
        lblTitulo.setFont(new Font("Arial Black", 26));
        lblTitulo.setTextFill(Color.WHITE);

        Button btnClientes = crearBoton("Gestión de Clientes");
        Button btnUsuarios = crearBoton("Gestión de Usuarios");
        Button btnAsistencia = crearBoton("Asistencia");
        Button btnMaquinas = crearBoton("Gestión de Máquinas");
        Button btnPagos = crearBoton("Pagos");
        Button btnReportes = crearBoton("Reportes");

        // Abrir ClienteUI al presionar "Gestión de Clientes"
        btnClientes.setOnAction(e -> new ClienteUI("Administrador").start(new Stage()));


        // Abrir AdminUI al presionar "Gestión de Usuarios"
        btnUsuarios.setOnAction(e -> new GestionUsuariosUI().start(new Stage()));

        VBox layout = new VBox(20, lblTitulo, btnClientes, btnUsuarios, btnAsistencia, btnMaquinas, btnPagos, btnReportes);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(50));
        layout.setStyle("-fx-background-color: black;");

        StackPane root = new StackPane(layout);
        Scene scene = new Scene(root, 600, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Button crearBoton(String texto) {
        Button boton = new Button(texto);
        boton.setPrefWidth(300);
        boton.setPrefHeight(45);
        boton.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-border-color: #1DB954;" +
            "-fx-border-width: 2;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 16px;" +
            "-fx-background-radius: 12;" +
            "-fx-border-radius: 12;"
        );
        return boton;
    }

    public static void main(String[] args) {
        launch(args);
    }
}

package ui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class UsuarioUI extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Gestión de Membresías");

        // ====================
        // Barra lateral
        // ====================
        VBox sideMenu = new VBox(20);
        sideMenu.setPadding(new Insets(20));
        sideMenu.setStyle("-fx-background-color: #2E2E2E;");

        Label lblMenu = new Label("Menú");
        lblMenu.setTextFill(Color.WHITE);
        lblMenu.setFont(Font.font("Arial", 18));

        Button btnInicio = new Button("Inicio");
        styleSideButton(btnInicio);
        Button btnUsuarios = new Button("Usuarios");
        styleSideButton(btnUsuarios);
        Button btnMembresias = new Button("Membresías");
        styleSideButton(btnMembresias);
        Button btnPagos = new Button("Pagos");
        styleSideButton(btnPagos);

        // Agregamos componentes a la barra lateral
        sideMenu.getChildren().addAll(lblMenu, btnInicio, btnUsuarios, btnMembresias, btnPagos);

        // ====================
        // Contenido central
        // ====================
        VBox mainContent = new VBox(20);
        mainContent.setPadding(new Insets(20));
        mainContent.setAlignment(Pos.TOP_CENTER);
        mainContent.setStyle("-fx-background-color: #1A1A1A;");

        // Título de la sección
        Label title = new Label("Gestión de Membresías");
        title.setFont(Font.font("Arial Black", 26));
        title.setTextFill(Color.WHITE);

        // Un subtítulo o descripción
        Label subtitle = new Label("Crea, actualiza y elimina membresías.");
        subtitle.setFont(Font.font("Arial", 14));
        subtitle.setTextFill(Color.LIGHTGRAY);

        // Botones de acción
        Button btnCrear = crearBotonColor("Crear Membresía", Color.web("#1DB954"));
        Button btnActualizar = crearBotonColor("Actualizar Membresía", Color.web("#F1C40F"));
        Button btnEliminar = crearBotonColor("Eliminar Membresía", Color.web("#E74C3C"));

        HBox actionButtons = new HBox(15, btnCrear, btnActualizar, btnEliminar);
        actionButtons.setAlignment(Pos.CENTER);

        // Tabla de ejemplo (podrías usar TableView para mostrar membresías registradas)
        TableView<String> tablaMembresias = new TableView<>();
        tablaMembresias.setPrefHeight(200);
        tablaMembresias.setStyle("-fx-background-color: #2E2E2E;");
        // (Aquí deberías configurar tus columnas y datos reales)

        // Añadimos componentes al contenido central
        mainContent.getChildren().addAll(title, subtitle, actionButtons, tablaMembresias);

        // ====================
        // Layout principal (BorderPane)
        // ====================
        BorderPane root = new BorderPane();
        root.setLeft(sideMenu);
        root.setCenter(mainContent);

        // Escena y Stage
        Scene scene = new Scene(root, 900, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void styleSideButton(Button button) {
        button.setMaxWidth(Double.MAX_VALUE);
        button.setFont(Font.font("Arial", 14));
        button.setTextFill(Color.WHITE);
        button.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-border-color: #3A3A3A;" +
            "-fx-border-width: 1;" +
            "-fx-background-radius: 4;" +
            "-fx-border-radius: 4;" +
            "-fx-cursor: hand;"
        );
    }

    private Button crearBotonColor(String texto, Color color) {
        Button btn = new Button(texto);
        btn.setPrefSize(170, 40);
        btn.setFont(Font.font("Arial", 14));
        btn.setTextFill(Color.WHITE);
        String colorHex = String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
        btn.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-border-color: " + colorHex + ";" +
                "-fx-border-width: 2;" +
                "-fx-background-radius: 12;" +
                "-fx-border-radius: 12;" +
                "-fx-cursor: hand;"
        );
        return btn;
    }

    public static void main(String[] args) {
        launch(args);
    }
}

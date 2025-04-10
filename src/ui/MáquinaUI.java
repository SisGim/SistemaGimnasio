package ui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import modelo.Maquina; 

public class MaquinaUI extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Titan Forge - Registro de Máquina");

        // Logo
        ImageView logo = new ImageView(new Image("file:resources/logo.png"));
        logo.setFitWidth(180); 
        logo.setPreserveRatio(true); 

        // Título para registrar la máquina
        Label title = new Label("REGISTRAR MÁQUINA");
        title.setFont(new Font("Arial Black", 38));
        title.setTextFill(Color.WHITE);

        //Campos de entrada
        TextField txtNombre = new TextField();
        txtNombre.setPromptText("Nombre de la máquina");
        styleInput(txtNombre);

        TextField txtGrupoMuscular = new TextField();
        txtGrupoMuscular.setPromptText("Grupo muscular");
        styleInput(txtGrupoMuscular);

        TextField txtUbicacion = new TextField();
        txtUbicacion.setPromptText("Ubicación en el gimnasio");
        styleInput(txtUbicacion);

        CheckBox chkEnUso = new CheckBox("¿Está en uso?");
        chkEnUso.setTextFill(Color.WHITE);

        Label lblMensaje = new Label();
        lblMensaje.setTextFill(Color.LIGHTGREEN);

       //Botón para guardar la máquina
        Button btnGuardar = new Button("Guardar máquina");
        btnGuardar.setPrefWidth(300);
        btnGuardar.setPrefHeight(45);
        btnGuardar.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-border-color: #1DB954;" +
                "-fx-border-width: 2;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 18px;" +
                "-fx-background-radius: 12;" +
                "-fx-border-radius: 12;"
        );

        // Acción al hacer clic en el botón
        btnGuardar.setOnAction(e -> {
            // Obtener los valores ingresados
            String nombre = txtNombre.getText().trim();
            String grupo = txtGrupoMuscular.getText().trim();
            String ubicacion = txtUbicacion.getText().trim();
            boolean enUso = chkEnUso.isSelected();

            // Para no permitir campos vacíos
            if (nombre.isEmpty() || grupo.isEmpty() || ubicacion.isEmpty()) {
                lblMensaje.setTextFill(Color.RED);
                lblMensaje.setText("Todos los campos deben estar completos.");
            } else {
                // Crear una nueva Máquina
                Maquina maquina = new Maquina(0, nombre, grupo, ubicacion, enUso);
                lblMensaje.setTextFill(Color.LIGHTGREEN);
                lblMensaje.setText("Máquina registrada: " + maquina.getNombre());
                // Agregar conexión a BDD (pendiente)
            }
        });

        // Formulario
        VBox formBox = new VBox(20,
                logo,
                title,
                txtNombre,
                txtGrupoMuscular,
                txtUbicacion,
                chkEnUso,
                btnGuardar,
                lblMensaje
        );

        formBox.setAlignment(Pos.CENTER);
        formBox.setPadding(new Insets(50)); 
        formBox.setStyle(
                "-fx-background-color: rgba(0,0,0,0.8); " +
                "-fx-background-radius: 25px;"
        );

        // Fondo negro
        StackPane root = new StackPane(formBox);
        root.setStyle("-fx-background-color: black;");

        // Mostrar escena
        Scene scene = new Scene(root, 1280, 720);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Estilo del texto
    private void styleInput(TextField textField) {
        textField.setStyle(
                "-fx-background-color: #1e1e1e;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 16px;" +
                "-fx-prompt-text-fill: gray;" +
                "-fx-background-radius: 12;" +
                "-fx-border-color: #3399FF;" +
                "-fx-border-radius: 12;"
        );
        textField.setMaxWidth(300);
        textField.setPrefHeight(40);
    }
  
    public static void main(String[] args) {
        launch(args);
    }
}

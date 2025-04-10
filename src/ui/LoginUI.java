package ui;

import database.UsuarioDAO;
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

public class LoginUI extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Titan Forge - Login");

        // Logo más grande
        ImageView logo = new ImageView(new Image("file:resources/logo.png")); // Asegúrate de que esta ruta exista
        logo.setFitWidth(180);
        logo.setPreserveRatio(true);

        Label title = new Label("LOGIN");
        title.setFont(new Font("Arial Black", 42));
        title.setTextFill(Color.WHITE);

        // Campos de entrada
        TextField txtUsuario = new TextField();
        txtUsuario.setPromptText("Username");
        styleInput(txtUsuario);

        PasswordField txtPassword = new PasswordField();
        txtPassword.setPromptText("Password");
        styleInput(txtPassword);

        Label lblMensajeError = new Label();
        lblMensajeError.setTextFill(Color.RED);

        // Hyperlink forgotPassword = new Hyperlink("Forgot password?");
        // forgotPassword.setTextFill(Color.LIGHTGRAY);
        // forgotPassword.setFont(new Font(14));

        // Botón de login más grande
        Button btnIniciarSesion = new Button("Login");
        btnIniciarSesion.setPrefWidth(300);
        btnIniciarSesion.setPrefHeight(45);
        btnIniciarSesion.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-border-color: #1DB954;" +
                "-fx-border-width: 2;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 18px;" +
                "-fx-background-radius: 12;" +
                "-fx-border-radius: 12;"
        );

        btnIniciarSesion.setOnAction(e -> {
            String username = txtUsuario.getText().trim();
            String password = txtPassword.getText().trim();

            if (username.isEmpty() || password.isEmpty()) {
                lblMensajeError.setText("Ingrese usuario y contraseña.");
                return;
            }

            String rol = UsuarioDAO.verificarCredenciales(username, password);
            if (rol != null) {
                new ClienteUI(rol).start(new Stage());
                primaryStage.close();
            } else {
                lblMensajeError.setText("Credenciales incorrectas o usuario bloqueado.");
            }
        });

        // Redes sociales (emojis como íconos)
        Label twitter = new Label("🐦");
        Label instagram = new Label("📸");
        Label youtube = new Label("▶️");

        twitter.setFont(new Font(28));
        instagram.setFont(new Font(28));
        youtube.setFont(new Font(28));

        HBox socialIcons = new HBox(30, twitter, instagram, youtube);
        socialIcons.setAlignment(Pos.CENTER);

        // Contenedor principal del login (centrado y grande)
        VBox loginBox = new VBox(25,
                logo, title,
                txtUsuario, txtPassword,
                // forgotPassword,
                btnIniciarSesion,
                lblMensajeError,
                socialIcons
        );
        loginBox.setAlignment(Pos.CENTER);
        loginBox.setPadding(new Insets(60));
        loginBox.setStyle("-fx-background-color: rgba(0,0,0,0.8); -fx-background-radius: 25px;");

        // Fondo negro y centrado
        StackPane root = new StackPane(loginBox);
        root.setStyle("-fx-background-color: black;");

        Scene scene = new Scene(root, 1280, 720);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

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

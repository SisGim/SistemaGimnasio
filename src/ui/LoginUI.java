package ui;

import database.UsuarioDAO;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.util.Optional;

public class LoginUI extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Titan Forge - Login");

        Label title = new Label("LOGIN");
        title.setFont(new Font("Arial Black", 42));
        title.setTextFill(Color.WHITE);

        TextField txtUsuario = new TextField();
        txtUsuario.setPromptText("Username");
        styleInput(txtUsuario);

        PasswordField txtPassword = new PasswordField();
        txtPassword.setPromptText("Password");
        styleInput(txtPassword);

        Label lblMensajeError = new Label();
        lblMensajeError.setTextFill(Color.RED);

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
                Stage nuevoStage = new Stage();
                if (rol.equalsIgnoreCase("Administrador")) {
                    new AdministradorUI().start(nuevoStage);
                } else {
                    new ClienteUI(rol).start(nuevoStage);
                }
                primaryStage.close();
            } else {
                lblMensajeError.setText("Credenciales incorrectas o usuario bloqueado.");
            }
        });

        Button btnRegistrarse = new Button("Registrarse");
        btnRegistrarse.setPrefWidth(300);
        btnRegistrarse.setPrefHeight(45);
        btnRegistrarse.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-border-color: #1DB954;" +
                "-fx-border-width: 2;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 18px;" +
                "-fx-background-radius: 12;" +
                "-fx-border-radius: 12;"
        );

        btnRegistrarse.setOnAction(e -> {
            Dialog<Pair<String, String>> dialog = new Dialog<>();
            dialog.setTitle("Registro de Usuario");
            dialog.setHeaderText("Ingrese los datos del nuevo usuario");

            ButtonType registerButtonType = new ButtonType("Registrar", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(registerButtonType, ButtonType.CANCEL);

            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20, 150, 10, 10));

            TextField usuario = new TextField();
            usuario.setPromptText("Usuario");

            PasswordField password = new PasswordField();
            password.setPromptText("Contraseña");

            grid.add(new Label("Usuario:"), 0, 0);
            grid.add(usuario, 1, 0);
            grid.add(new Label("Contraseña:"), 0, 1);
            grid.add(password, 1, 1);

            dialog.getDialogPane().setContent(grid);
            Platform.runLater(usuario::requestFocus);

            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == registerButtonType) {
                    return new Pair<>(usuario.getText(), password.getText());
                }
                return null;
            });

            Optional<Pair<String, String>> result = dialog.showAndWait();

            result.ifPresent(credentials -> {
                String user = credentials.getKey();
                String pass = credentials.getValue();
                boolean exito = UsuarioDAO.registrarUsuario(user, pass);
                Alert alert = new Alert(exito ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR);
                alert.setTitle(exito ? "Registro exitoso" : "Error");
                alert.setHeaderText(null);
                alert.setContentText(exito ? "Usuario registrado correctamente." : "El usuario ya existe o no se pudo registrar.");
                alert.showAndWait();
            });
        });

        Label twitter = new Label("🐦");
        Label instagram = new Label("📸");
        Label youtube = new Label("▶️");

        twitter.setFont(new Font(28));
        instagram.setFont(new Font(28));
        youtube.setFont(new Font(28));

        HBox socialIcons = new HBox(30, twitter, instagram, youtube);
        socialIcons.setAlignment(Pos.CENTER);

        VBox loginBox = new VBox(25,
                title,
                txtUsuario,
                txtPassword,
                btnIniciarSesion,
                btnRegistrarse,
                lblMensajeError,
                socialIcons
        );
        loginBox.setAlignment(Pos.CENTER);
        loginBox.setPadding(new Insets(60));
        loginBox.setStyle("-fx-background-color: rgba(0,0,0,0.8); -fx-background-radius: 25px;");

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

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
import java.util.regex.Pattern;

public class LoginUI extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Titan Forge - Login");

        Label title = new Label("LOGIN");
        title.setFont(new Font("Arial Black", 42));
        title.setTextFill(Color.WHITE);

        TextField txtEmail = new TextField();
        txtEmail.setPromptText("Correo electrónico");
        styleInput(txtEmail);

        PasswordField txtPassword = new PasswordField();
        txtPassword.setPromptText("Contraseña");
        styleInput(txtPassword);

        Label lblMensajeError = new Label();
        lblMensajeError.setTextFill(Color.RED);

        Button btnIniciarSesion = new Button("Login");
        btnIniciarSesion.setPrefWidth(300);
        btnIniciarSesion.setPrefHeight(45);
        btnIniciarSesion.setStyle(buttonStyle());

        btnIniciarSesion.setOnAction(e -> {
            String email = txtEmail.getText().trim();
            String password = txtPassword.getText().trim();

            if (email.isEmpty() || password.isEmpty()) {
                lblMensajeError.setText("Ingrese correo y contraseña.");
                return;
            }

            String rol = UsuarioDAO.verificarCredenciales(email, password);
            if (rol != null) {
                Stage nuevoStage = new Stage();
                if (rol.equalsIgnoreCase("Administrador")) {
                    new AdministradorUI().start(nuevoStage);
                } else {
                    new ClienteUI(rol).start(nuevoStage);
                }
                primaryStage.close();
            } else {
                lblMensajeError.setText("Credenciales incorrectas o usuario no registrado.");
            }
        });

        Button btnRegistrarse = new Button("Registrarse");
        btnRegistrarse.setPrefWidth(300);
        btnRegistrarse.setPrefHeight(45);
        btnRegistrarse.setStyle(buttonStyle());

        btnRegistrarse.setOnAction(e -> mostrarVentanaRegistro());

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
                txtEmail,
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

    private void mostrarVentanaRegistro() {
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Registro de Usuario");
        dialog.setHeaderText("Ingrese su correo y contraseña");

        ButtonType registerButtonType = new ButtonType("Registrar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(registerButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField email = new TextField();
        email.setPromptText("Correo electrónico");

        PasswordField password = new PasswordField();
        password.setPromptText("Contraseña");

        grid.add(new Label("Correo:"), 0, 0);
        grid.add(email, 1, 0);
        grid.add(new Label("Contraseña:"), 0, 1);
        grid.add(password, 1, 1);

        dialog.getDialogPane().setContent(grid);
        Platform.runLater(email::requestFocus);

        // 🔁 Manejador del botón personalizado (para mantener abierta la ventana si es inválido)
        final Button btnRegistrar = (Button) dialog.getDialogPane().lookupButton(registerButtonType);
        btnRegistrar.addEventFilter(javafx.event.ActionEvent.ACTION, event -> {
            String correo = email.getText().trim();
            String pass = password.getText().trim();

            if (correo.isEmpty() || pass.isEmpty()) {
                mostrarAlerta(Alert.AlertType.ERROR, "Campos incompletos", "Debes ingresar correo y contraseña.");
                event.consume(); // No cierra la ventana
                return;
            }

            if (!validarPassword(pass)) {
                mostrarAlerta(Alert.AlertType.ERROR, "Contraseña insegura", """
                        La contraseña debe tener al menos 12 caracteres, 
                        incluir una mayúscula, una minúscula, un número y un símbolo.""");
                event.consume(); // No cierra la ventana
                return;
            }

            boolean exito = UsuarioDAO.registrarUsuario(correo, pass);
            Alert.AlertType tipo = exito ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR;
            String mensaje = exito
                    ? "Registro exitoso. Se ha enviado un correo de confirmación."
                    : "El correo ya está registrado o hubo un error.";
            mostrarAlerta(tipo, exito ? "Éxito" : "Error", mensaje);

            if (!exito) event.consume(); // No cerrar si falla el registro
        });

        dialog.showAndWait();
    }

    private boolean validarPassword(String password) {
        return password.length() >= 12 &&
                Pattern.compile("[A-Z]").matcher(password).find() &&
                Pattern.compile("[a-z]").matcher(password).find() &&
                Pattern.compile("\\d").matcher(password).find() &&
                Pattern.compile("[^a-zA-Z0-9]").matcher(password).find();
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
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

    private String buttonStyle() {
        return "-fx-background-color: transparent;" +
               "-fx-border-color: #1DB954;" +
               "-fx-border-width: 2;" +
               "-fx-text-fill: white;" +
               "-fx-font-size: 18px;" +
               "-fx-background-radius: 12;" +
               "-fx-border-radius: 12;";
    }

    public static void main(String[] args) {
        launch(args);
    }
}

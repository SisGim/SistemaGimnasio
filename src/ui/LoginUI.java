package ui;

import database.UsuarioDAO;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoginUI extends Application { // 📌 Extiende de Application

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Login - Gimnasio");

        Label lblUsuario = new Label("Usuario:");
        TextField txtUsuario = new TextField();
        Label lblPassword = new Label("Contraseña:");
        PasswordField txtPassword = new PasswordField();
        Button btnIniciarSesion = new Button("Iniciar Sesión");
        Label lblMensajeError = new Label();
        lblMensajeError.setStyle("-fx-text-fill: red;");

        btnIniciarSesion.setOnAction(e -> {
            String username = txtUsuario.getText().trim();
            String password = txtPassword.getText().trim();

            if (username.isEmpty() || password.isEmpty()) {
                lblMensajeError.setText("Ingrese usuario y contraseña.");
                return;
            }

            // 📌 Verificar credenciales en la base de datos
            String rol = UsuarioDAO.verificarCredenciales(username, password);
            if (rol != null) {
                System.out.println("Inicio de sesión exitoso. Rol: " + rol);
                new ClienteUI(rol).start(new Stage()); // 📌 Pasa el rol a la pantalla de gestión de clientes
                primaryStage.close();
            } else {
                lblMensajeError.setText("Credenciales incorrectas o usuario bloqueado.");
            }
        });

        VBox layout = new VBox(10, lblUsuario, txtUsuario, lblPassword, txtPassword, btnIniciarSesion, lblMensajeError);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-padding: 20px;");

        Scene scene = new Scene(layout, 300, 250);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // 📌 Método main para iniciar JavaFX correctamente
    public static void main(String[] args) {
        launch(args);
    }
}

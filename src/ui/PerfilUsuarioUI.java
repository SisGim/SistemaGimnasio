package ui;

import database.ClienteDAO;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import models.Cliente;

public class PerfilUsuarioUI extends VBox {

    public PerfilUsuarioUI(String correoUsuario) {
        setPadding(new Insets(30));
        setSpacing(20);
        setAlignment(Pos.TOP_CENTER);
        setStyle("-fx-background-color: black;");

        Label titulo = new Label("Editar Perfil de Usuario");
        titulo.setFont(new Font("Arial Black", 22));
        titulo.setTextFill(Color.WHITE);

        TextField txtNombre = new TextField();
        txtNombre.setPromptText("Nombre completo");
        styleInput(txtNombre);

        TextField txtTelefono = new TextField();
        txtTelefono.setPromptText("Teléfono");
        styleInput(txtTelefono);

        TextField txtCorreo = new TextField(correoUsuario);
        txtCorreo.setPromptText("Correo electrónico");
        txtCorreo.setDisable(true);
        styleInput(txtCorreo);

        TextField txtIdentificacion = new TextField();
        txtIdentificacion.setPromptText("Identificación");
        styleInput(txtIdentificacion);

        Button btnActualizar = new Button("Actualizar Perfil");
        btnActualizar.setPrefWidth(200);
        btnActualizar.setStyle("-fx-background-color: #1DB954; -fx-text-fill: white; -fx-font-size: 16px;");

        Label lblResultado = new Label();
        lblResultado.setTextFill(Color.LIGHTGREEN);

        // Intenta precargar los datos del cliente
        Cliente cliente = ClienteDAO.obtenerClientePorCorreo(correoUsuario);
        if (cliente != null) {
            txtNombre.setText(cliente.getNombre());
            txtTelefono.setText(cliente.getTelefono());
            txtIdentificacion.setText(cliente.getIdentificacion());
        }

        btnActualizar.setOnAction(e -> {
            String nombre = txtNombre.getText().trim();
            String telefono = txtTelefono.getText().trim();
            String identificacion = txtIdentificacion.getText().trim();

            if (nombre.isEmpty() || telefono.isEmpty() || identificacion.isEmpty()) {
                lblResultado.setText("Por favor completa todos los campos.");
                lblResultado.setTextFill(Color.RED);
                return;
            }

            Cliente actualizado = new Cliente(
                    cliente != null ? cliente.getId() : 0,
                    nombre, telefono, correoUsuario, 
                    cliente != null ? cliente.getMembresia() : "Básica",
                    identificacion
            );

            boolean exito = ClienteDAO.actualizarClientePorCorreo(actualizado);

            if (exito) {
                lblResultado.setText("✅ Perfil actualizado correctamente.");
                lblResultado.setTextFill(Color.LIGHTGREEN);
            } else {
                lblResultado.setText("❌ No se encontró una entrada de cliente con ese correo.");
                lblResultado.setTextFill(Color.RED);
            }
        });

        getChildren().addAll(
                titulo,
                txtNombre, txtTelefono,
                txtCorreo, txtIdentificacion,
                btnActualizar, lblResultado
        );
    }

    private void styleInput(TextField textField) {
        textField.setStyle(
                "-fx-background-color: #1e1e1e;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 14px;" +
                "-fx-prompt-text-fill: gray;" +
                "-fx-background-radius: 10;" +
                "-fx-border-color: #3399FF;" +
                "-fx-border-radius: 10;"
        );
        textField.setMaxWidth(300);
        textField.setPrefHeight(40);
    }
}

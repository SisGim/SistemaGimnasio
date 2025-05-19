package ui;

import database.ClienteDAO;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
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

        // Usamos un array para poder modificar el cliente dentro del lambda
        final Cliente[] clienteRef = new Cliente[1];
        clienteRef[0] = ClienteDAO.obtenerClientePorCorreo(correoUsuario);
        if (clienteRef[0] == null) {
            clienteRef[0] = new Cliente(0, "", "", correoUsuario, "Básica", "");
        }

        // Campos
        TextField txtNombre = new TextField(filtrarCampo(clienteRef[0].getNombre()));
        txtNombre.setPromptText("Nombre completo");
        styleInput(txtNombre);

        TextField txtTelefono = new TextField(filtrarCampo(clienteRef[0].getTelefono()));
        txtTelefono.setPromptText("Teléfono");
        styleInput(txtTelefono);

        TextField txtCorreo = new TextField(correoUsuario);
        txtCorreo.setDisable(true);
        styleInput(txtCorreo);

        TextField txtIdentificacion = new TextField(filtrarCampo(clienteRef[0].getIdentificacion()));
        txtIdentificacion.setPromptText("Identificación");
        styleInput(txtIdentificacion);

        // Etiquetas
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(15);
        grid.setAlignment(Pos.CENTER);

        grid.add(crearLabel("Nombre:"), 0, 0); grid.add(txtNombre, 1, 0);
        grid.add(crearLabel("Teléfono:"), 0, 1); grid.add(txtTelefono, 1, 1);
        grid.add(crearLabel("Correo:"), 0, 2); grid.add(txtCorreo, 1, 2);
        grid.add(crearLabel("Identificación:"), 0, 3); grid.add(txtIdentificacion, 1, 3);

        Button btnActualizar = new Button("Actualizar Perfil");
        btnActualizar.setPrefWidth(200);
        btnActualizar.setStyle("-fx-background-color: #1DB954; -fx-text-fill: white; -fx-font-size: 16px;");

        Label lblResultado = new Label();
        lblResultado.setTextFill(Color.LIGHTGREEN);

        btnActualizar.setOnAction(e -> {
            String nombre = txtNombre.getText().trim();
            String telefono = txtTelefono.getText().trim();
            String identificacion = txtIdentificacion.getText().trim();

            if (nombre.isEmpty() || telefono.isEmpty() || identificacion.isEmpty()) {
                lblResultado.setText("Por favor completa todos los campos.");
                lblResultado.setTextFill(Color.RED);
                return;
            }

            clienteRef[0].setNombre(nombre);
            clienteRef[0].setTelefono(telefono);
            clienteRef[0].setIdentificacion(identificacion);

            boolean exito = ClienteDAO.actualizarClientePorCorreo(clienteRef[0]);

            if (exito) {
                lblResultado.setText("✅ Perfil actualizado correctamente.");
                lblResultado.setTextFill(Color.LIGHTGREEN);
            } else {
                lblResultado.setText("❌ Error al actualizar el perfil.");
                lblResultado.setTextFill(Color.RED);
            }
        });

        getChildren().addAll(titulo, grid, btnActualizar, lblResultado);
    }

    private String filtrarCampo(String valor) {
        return (valor == null || valor.equalsIgnoreCase("por completar")) ? "" : valor;
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

    private Label crearLabel(String texto) {
        Label label = new Label(texto);
        label.setTextFill(Color.WHITE);
        label.setFont(new Font(16));
        return label;
    }
}

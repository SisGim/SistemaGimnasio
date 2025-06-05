package ui;

import database.ClienteDAO;
import database.UsuarioDAO;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import models.Cliente;
import models.Usuario;

public class PerfilUsuarioUI extends VBox {

    public PerfilUsuarioUI(String correoUsuario) {
        setPadding(new Insets(30));
        setSpacing(20);
        setAlignment(Pos.TOP_CENTER);
        setStyle("-fx-background-color: black;");

        Label titulo = new Label("Editar Perfil de Usuario");
        titulo.setFont(new Font("Arial Black", 22));
        titulo.setTextFill(Color.WHITE);

        Label lblResultado = new Label();
        lblResultado.setTextFill(Color.LIGHTGREEN);

        // Cargar usuario
        Cliente cliente = ClienteDAO.obtenerClientePorCorreo(correoUsuario);
        Usuario usuario = UsuarioDAO.obtenerUsuarioPorCorreo(correoUsuario);
        boolean esCliente = (cliente != null);

        if (!esCliente && usuario == null) {
            lblResultado.setText("❌ No se encontró el perfil.");
            lblResultado.setTextFill(Color.RED);
            getChildren().addAll(titulo, lblResultado);
            return;
        }

        String nombre = esCliente ? cliente.getNombre() : usuario.getNombre();
        String telefono = esCliente ? cliente.getTelefono() : usuario.getTelefono();
        String identificacion = esCliente ? cliente.getIdentificacion() : usuario.getIdentificacion();

        // Campos
        TextField txtNombre = new TextField(filtrarCampo(nombre));
        txtNombre.setPromptText("Nombre completo");
        styleInput(txtNombre);

        TextField txtTelefono = new TextField(filtrarCampo(telefono));
        txtTelefono.setPromptText("Teléfono");
        styleInput(txtTelefono);

        TextField txtCorreo = new TextField(correoUsuario);
        txtCorreo.setDisable(true);
        styleInput(txtCorreo);

        TextField txtIdentificacion = new TextField(filtrarCampo(identificacion));
        txtIdentificacion.setPromptText("Identificación");
        styleInput(txtIdentificacion);

        // Layout campos
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(15);
        grid.setAlignment(Pos.CENTER);

        grid.add(crearLabel("Nombre:"), 0, 0); grid.add(txtNombre, 1, 0);
        grid.add(crearLabel("Teléfono:"), 0, 1); grid.add(txtTelefono, 1, 1);
        grid.add(crearLabel("Correo:"), 0, 2); grid.add(txtCorreo, 1, 2);
        grid.add(crearLabel("Identificación:"), 0, 3); grid.add(txtIdentificacion, 1, 3);

        // Botón actualizar
        Button btnActualizar = new Button("Actualizar Perfil");
        btnActualizar.setPrefWidth(200);
        btnActualizar.setStyle("-fx-background-color: #1DB954; -fx-text-fill: white; -fx-font-size: 16px;");

        btnActualizar.setOnAction(e -> {
            String nuevoNombre = txtNombre.getText().trim();
            String nuevoTel = txtTelefono.getText().trim();
            String nuevaId = txtIdentificacion.getText().trim();

            if (nuevoNombre.isEmpty() || nuevoTel.isEmpty() || nuevaId.isEmpty()) {
                lblResultado.setText("Por favor completa todos los campos.");
                lblResultado.setTextFill(Color.RED);
                return;
            }

            boolean actualizado;
            if (esCliente) {
                cliente.setNombre(nuevoNombre);
                cliente.setTelefono(nuevoTel);
                cliente.setIdentificacion(nuevaId);
                actualizado = ClienteDAO.actualizarClientePorCorreo(cliente);
            } else {
                usuario.setNombre(nuevoNombre);
                usuario.setTelefono(nuevoTel);
                usuario.setIdentificacion(nuevaId);
                actualizado = UsuarioDAO.actualizarUsuarioPorCorreo(usuario);
            }

            if (actualizado) {
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

package ui;

import database.UsuarioDAO;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.util.List;

public class GestionUsuariosUI {

    public Node getVista() {
        Label lblTitulo = new Label("GESTIÓN DE ROLES DE USUARIOS");
        lblTitulo.setStyle("-fx-font-size: 22px; -fx-text-fill: white;");

        ComboBox<String> cmbUsuarios = new ComboBox<>();
        List<String> usuarios = UsuarioDAO.obtenerUsuarios();
        cmbUsuarios.getItems().addAll(usuarios);
        cmbUsuarios.setPromptText("Selecciona un usuario");

        Label lblRolActual = new Label("Rol actual: ");
        lblRolActual.setStyle("-fx-text-fill: white;");

        ComboBox<String> cmbRoles = new ComboBox<>();
        cmbRoles.getItems().addAll("Administrador", "Empleado", "Cliente", "Entrenador"); // ✅ Incluido nuevo rol
        cmbRoles.setPromptText("Nuevo rol");

        Label lblResultado = new Label();
        lblResultado.setStyle("-fx-text-fill: lightgreen;");

        cmbUsuarios.setOnAction(e -> {
            String seleccionado = cmbUsuarios.getValue();
            if (seleccionado != null) {
                String rolActual = UsuarioDAO.obtenerRol(seleccionado);
                lblRolActual.setText("Rol actual: " + rolActual);
            }
        });

        Button btnActualizar = new Button("Actualizar Rol");
        btnActualizar.setStyle("-fx-background-color: #1DB954; -fx-text-fill: white;");
        btnActualizar.setOnAction(e -> {
            String usuario = cmbUsuarios.getValue();
            String nuevoRol = cmbRoles.getValue();

            if (usuario == null || nuevoRol == null) {
                lblResultado.setStyle("-fx-text-fill: red;");
                lblResultado.setText("Debes seleccionar usuario y rol.");
                return;
            }

            boolean exito = UsuarioDAO.actualizarRol(usuario, nuevoRol);
            if (exito) {
                lblResultado.setStyle("-fx-text-fill: lightgreen;");
                lblResultado.setText("Rol actualizado correctamente.");
                lblRolActual.setText("Rol actual: " + nuevoRol);
            } else {
                lblResultado.setStyle("-fx-text-fill: red;");
                lblResultado.setText("Error al actualizar el rol.");
            }
        });

        VBox layout = new VBox(15, lblTitulo, cmbUsuarios, lblRolActual, cmbRoles, btnActualizar, lblResultado);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(40));
        layout.setStyle("-fx-background-color: black;");
        return layout;
    }
}

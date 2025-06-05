package ui;

import database.UsuarioDAO;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import models.Usuario;
import util.CustomDialogUtil;

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
        cmbRoles.getItems().addAll("Administrador", "Empleado", "Cliente", "Entrenador");
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

        // 🔽 Registro de nuevos empleados
        Label lblRegistro = new Label("REGISTRO DE NUEVO EMPLEADO");
        lblRegistro.setStyle("-fx-font-size: 20px; -fx-text-fill: white; -fx-padding: 20 0 0 0;");

        TextField campoCorreo = new TextField(); campoCorreo.setPromptText("Correo");
        TextField campoNombre = new TextField(); campoNombre.setPromptText("Nombre completo");
        TextField campoIdentificacion = new TextField(); campoIdentificacion.setPromptText("Identificación");
        TextField campoTelefono = new TextField(); campoTelefono.setPromptText("Teléfono");
        PasswordField campoPassword = new PasswordField(); campoPassword.setPromptText("Contraseña");

        Button btnRegistrarEmpleado = new Button("Registrar Empleado");
        btnRegistrarEmpleado.setStyle("-fx-background-color: green; -fx-text-fill: white;");
        btnRegistrarEmpleado.setOnAction(e -> {
            if (campoCorreo.getText().isEmpty() || campoNombre.getText().isEmpty() ||
                campoIdentificacion.getText().isEmpty() || campoTelefono.getText().isEmpty() ||
                campoPassword.getText().isEmpty()) {
                CustomDialogUtil.mostrarAlertaEstilizada("Completa todos los campos para registrar al empleado.");
                return;
            }

            Usuario nuevoEmpleado = new Usuario(
                    0,
                    campoNombre.getText(),
                    campoCorreo.getText(),
                    "Empleado",
                    campoPassword.getText(),
                    campoTelefono.getText(),
                    campoIdentificacion.getText()
            );

            boolean registrado = UsuarioDAO.registrarEmpleado(nuevoEmpleado);
            if (registrado) {
                CustomDialogUtil.mostrarAlertaEstilizada("✅ Empleado registrado exitosamente.");
                campoCorreo.clear(); campoNombre.clear(); campoIdentificacion.clear(); campoTelefono.clear(); campoPassword.clear();
                cmbUsuarios.getItems().add(nuevoEmpleado.getEmail());
            } else {
                CustomDialogUtil.mostrarAlertaEstilizada("❌ Error al registrar al empleado.");
            }
        });

        VBox layout = new VBox(15,
                lblTitulo, cmbUsuarios, lblRolActual, cmbRoles, btnActualizar, lblResultado,
                lblRegistro, campoCorreo, campoNombre, campoIdentificacion, campoTelefono, campoPassword, btnRegistrarEmpleado
        );

        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(40));
        layout.setStyle("-fx-background-color: black;");

        return layout;
    }
}

package ui;

import database.UsuarioDAO;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.ScrollPane;
import models.Usuario;
import util.CustomDialogUtil;

public class GestionUsuariosUI {

    public Node getVista() {
        // --- Sección de roles ---
        Label lblTitulo = new Label("GESTIÓN DE ROLES DE USUARIOS");
        lblTitulo.setStyle("-fx-font-size: 22px; -fx-text-fill: white;");

        ComboBox<String> cmbUsuarios = new ComboBox<>();
        cmbUsuarios.setPromptText("Selecciona un usuario");
        cmbUsuarios.setMaxWidth(400);
        cmbUsuarios.getItems().addAll(UsuarioDAO.obtenerUsuarios());

        Label lblRolActual = new Label("Rol actual: ");
        lblRolActual.setStyle("-fx-text-fill: white;");

        ComboBox<String> cmbRoles = new ComboBox<>();
        cmbRoles.getItems().addAll("Administrador", "Empleado", "Cliente", "Entrenador");
        cmbRoles.setPromptText("Nuevo rol");
        cmbRoles.setMaxWidth(400);

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

        // --- Combo de inactivos (declaración antes de uso) ---
        ComboBox<String> cmbInactivos = new ComboBox<>();
        cmbInactivos.setPromptText("Selecciona un empleado inactivo");
        cmbInactivos.setMaxWidth(400);
        cmbInactivos.getItems().addAll(
                UsuarioDAO.obtenerCorreosPorRol("Empleado").stream()
                        .filter(correo -> {
                            Usuario u = UsuarioDAO.obtenerUsuarioPorCorreo(correo);
                            return u != null && "inactivo".equalsIgnoreCase(u.getEstado());
                        })
                        .toList()
        );

        // --- Registro nuevo empleado ---
        Label lblRegistro = new Label("REGISTRO DE NUEVO EMPLEADO");
        lblRegistro.setStyle("-fx-font-size: 20px; -fx-text-fill: white;");

        TextField campoCorreo = new TextField(); campoCorreo.setPromptText("Correo"); campoCorreo.setMaxWidth(400);
        TextField campoNombre = new TextField(); campoNombre.setPromptText("Nombre completo"); campoNombre.setMaxWidth(400);
        TextField campoIdentificacion = new TextField(); campoIdentificacion.setPromptText("Identificación"); campoIdentificacion.setMaxWidth(400);
        TextField campoTelefono = new TextField(); campoTelefono.setPromptText("Teléfono"); campoTelefono.setMaxWidth(400);
        PasswordField campoPassword = new PasswordField(); campoPassword.setPromptText("Contraseña"); campoPassword.setMaxWidth(400);

        ComboBox<String> cmbEmpleados = new ComboBox<>();
        cmbEmpleados.setPromptText("Selecciona un empleado activo");
        cmbEmpleados.setMaxWidth(400);
        cmbEmpleados.getItems().addAll(UsuarioDAO.obtenerEmpleadosActivos());

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
                cmbEmpleados.getItems().add(nuevoEmpleado.getEmail());
                cmbInactivos.getItems().remove(nuevoEmpleado.getEmail());
            } else {
                CustomDialogUtil.mostrarAlertaEstilizada("❌ Error al registrar al empleado.");
            }
        });

        // --- Gestión empleados activos ---
        Label lblGestion = new Label("GESTIÓN DE EMPLEADOS ACTIVOS");
        lblGestion.setStyle("-fx-font-size: 20px; -fx-text-fill: white;");

        TextField txtNombre = new TextField(); txtNombre.setPromptText("Nombre"); txtNombre.setMaxWidth(400);
        TextField txtTelefono = new TextField(); txtTelefono.setPromptText("Teléfono"); txtTelefono.setMaxWidth(400);
        TextField txtIdentificacion = new TextField(); txtIdentificacion.setPromptText("Identificación"); txtIdentificacion.setMaxWidth(400);

        Button btnGuardarCambios = new Button("Guardar Cambios");
        btnGuardarCambios.setStyle("-fx-background-color: #ffaa00; -fx-text-fill: black;");
        btnGuardarCambios.setOnAction(e -> {
            String correo = cmbEmpleados.getValue();
            if (correo == null) {
                CustomDialogUtil.mostrarAlertaEstilizada("Selecciona un empleado para editar.");
                return;
            }

            Usuario actualizado = new Usuario(
                    0,
                    txtNombre.getText(),
                    correo,
                    "Empleado",
                    "",
                    txtTelefono.getText(),
                    txtIdentificacion.getText()
            );

            boolean ok = UsuarioDAO.modificarEmpleado(actualizado);
            if (ok) {
                CustomDialogUtil.mostrarAlertaEstilizada("✅ Datos actualizados correctamente.");
            } else {
                CustomDialogUtil.mostrarAlertaEstilizada("❌ No se pudo actualizar el perfil.");
            }
        });

        Button btnDesactivar = new Button("Desactivar Empleado");
        btnDesactivar.setStyle("-fx-background-color: darkred; -fx-text-fill: white;");
        btnDesactivar.setOnAction(e -> {
            String correo = cmbEmpleados.getValue();
            if (correo == null) {
                CustomDialogUtil.mostrarAlertaEstilizada("Selecciona un empleado para desactivar.");
                return;
            }

            boolean ok = UsuarioDAO.desactivarEmpleado(correo);
            if (ok) {
                CustomDialogUtil.mostrarAlertaEstilizada("🛑 Empleado desactivado correctamente.");
                cmbEmpleados.getItems().remove(correo);
                cmbInactivos.getItems().add(correo);
            } else {
                CustomDialogUtil.mostrarAlertaEstilizada("❌ No se pudo desactivar el empleado.");
            }
        });

        cmbEmpleados.setOnAction(e -> {
            String correo = cmbEmpleados.getValue();
            if (correo != null) {
                Usuario empleado = UsuarioDAO.obtenerUsuarioPorCorreo(correo);
                if (empleado != null) {
                    txtNombre.setText(empleado.getNombre());
                    txtTelefono.setText(empleado.getTelefono());
                    txtIdentificacion.setText(empleado.getIdentificacion());
                }
            }
        });

        // --- Reactivar empleados inactivos ---
        Label lblInactivos = new Label("EMPLEADOS DESACTIVADOS");
        lblInactivos.setStyle("-fx-font-size: 20px; -fx-text-fill: white;");

        Button btnReactivar = new Button("Reactivar Empleado");
        btnReactivar.setStyle("-fx-background-color: #007acc; -fx-text-fill: white;");
        btnReactivar.setOnAction(e -> {
            String correo = cmbInactivos.getValue();
            if (correo == null) {
                CustomDialogUtil.mostrarAlertaEstilizada("Selecciona un empleado para reactivar.");
                return;
            }

            boolean reactivado = UsuarioDAO.reactivarEmpleado(correo);
            if (reactivado) {
                CustomDialogUtil.mostrarAlertaEstilizada("✅ Empleado reactivado exitosamente.");
                cmbInactivos.getItems().remove(correo);
                cmbEmpleados.getItems().add(correo);
            } else {
                CustomDialogUtil.mostrarAlertaEstilizada("❌ No se pudo reactivar el empleado.");
            }
        });

        // --- Layout con scroll ---
        VBox layout = new VBox(15,
                lblTitulo, cmbUsuarios, lblRolActual, cmbRoles, btnActualizar, lblResultado,
                lblRegistro, campoCorreo, campoNombre, campoIdentificacion, campoTelefono, campoPassword, btnRegistrarEmpleado,
                lblGestion, cmbEmpleados, txtNombre, txtTelefono, txtIdentificacion, btnGuardarCambios, btnDesactivar,
                lblInactivos, cmbInactivos, btnReactivar
        );

        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(40));
        layout.setStyle("-fx-background-color: black;");

        ScrollPane scrollPane = new ScrollPane(layout);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: black;");

        return scrollPane;
    }
}

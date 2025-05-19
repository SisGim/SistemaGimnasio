package ui;

import database.ClienteDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import models.Cliente;

import java.util.Comparator;
import java.util.regex.Pattern;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ClienteUI {

    private TableView<Cliente> tableView = new TableView<>();
    private ObservableList<Cliente> clientesList = FXCollections.observableArrayList();
    private String rolUsuario;

    public ClienteUI(String rolUsuario) {
        this.rolUsuario = rolUsuario;
    }

    public Node getVista() {
        configurarTabla();

        Button btnAgregar = new Button("Agregar Cliente");
        Button btnModificar = new Button("Modificar Cliente");
        Button btnEliminar = new Button("Eliminar Cliente");

        if (!"Administrador".equals(rolUsuario)) {
            btnEliminar.setDisable(true);
        }

        clientesList.addAll(ClienteDAO.obtenerClientes());
        ordenarClientes();
        tableView.setItems(clientesList);

        btnAgregar.setOnAction(e -> mostrarFormularioCliente(null));
        btnModificar.setOnAction(e -> {
            Cliente seleccionado = tableView.getSelectionModel().getSelectedItem();
            if (seleccionado != null) {
                mostrarFormularioCliente(seleccionado);
            } else {
                mostrarError("Seleccione un cliente para modificar.");
            }
        });
        btnEliminar.setOnAction(e -> eliminarCliente());

        VBox layout = new VBox(10, tableView, btnAgregar, btnModificar, btnEliminar);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-padding: 20px; -fx-background-color: black;");
        return layout;
    }

    private void configurarTabla() {
        tableView.getColumns().clear();

        TableColumn<Cliente, Number> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(cellData -> cellData.getValue().idProperty());

        TableColumn<Cliente, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(cellData -> cellData.getValue().nombreProperty());

        TableColumn<Cliente, String> colTelefono = new TableColumn<>("Teléfono");
        colTelefono.setCellValueFactory(cellData -> cellData.getValue().telefonoProperty());

        TableColumn<Cliente, String> colEmail = new TableColumn<>("Email");
        colEmail.setCellValueFactory(cellData -> cellData.getValue().emailProperty());

        TableColumn<Cliente, String> colMembresia = new TableColumn<>("Membresía");
        colMembresia.setCellValueFactory(cellData -> cellData.getValue().membresiaProperty());

        TableColumn<Cliente, String> colIdentificacion = new TableColumn<>("Identificación");
        colIdentificacion.setCellValueFactory(cellData -> cellData.getValue().identificacionProperty());

        tableView.getColumns().addAll(colId, colNombre, colTelefono, colEmail, colMembresia, colIdentificacion);
    }

    private void mostrarFormularioCliente(Cliente clienteExistente) {
        Stage ventana = new Stage();
        boolean esNuevo = clienteExistente == null;

        ventana.setTitle(esNuevo ? "Agregar Cliente" : "Modificar Cliente");

        Label lblNombre = new Label("Nombre:");
        TextField txtNombre = new TextField(esNuevo ? "" : clienteExistente.getNombre());

        Label lblTelefono = new Label("Teléfono:");
        TextField txtTelefono = new TextField(esNuevo ? "" : clienteExistente.getTelefono());

        Label lblEmail = new Label("Email:");
        TextField txtEmail = new TextField(esNuevo ? "" : clienteExistente.getEmail());

        Label lblMembresia = new Label("Membresía:");
        ComboBox<String> cmbMembresia = new ComboBox<>();
        cmbMembresia.getItems().addAll("Básica", "Premium", "VIP", "Familiar");
        cmbMembresia.setValue(esNuevo ? "Básica" : clienteExistente.getMembresia());

        Label lblIdentificacion = new Label("Identificación:");
        TextField txtIdentificacion = new TextField(esNuevo ? "" : clienteExistente.getIdentificacion());

        Button btnGuardar = new Button(esNuevo ? "Guardar" : "Guardar Cambios");
        btnGuardar.setOnAction(e -> {
            String nombre = txtNombre.getText().trim();
            String telefono = txtTelefono.getText().trim();
            String email = txtEmail.getText().trim();
            String membresia = cmbMembresia.getValue();
            String identificacion = txtIdentificacion.getText().trim();

            if (nombre.isEmpty() || telefono.isEmpty() || email.isEmpty() || membresia == null || identificacion.isEmpty()) {
                mostrarError("Por favor, llena todos los campos.");
                return;
            }

            if (!validarTelefono(telefono)) {
                mostrarError("El teléfono solo debe contener números.");
                return;
            }

            if (!validarEmail(email)) {
                mostrarError("Email no válido.");
                return;
            }

            if (esNuevo) {
                Cliente nuevoCliente = new Cliente(0, nombre, telefono, email, membresia, identificacion);
                ClienteDAO.agregarCliente(nuevoCliente);
                mostrarInfo("Cliente agregado exitosamente.");
            } else {
                clienteExistente.setNombre(nombre);
                clienteExistente.setTelefono(telefono);
                clienteExistente.setEmail(email);
                clienteExistente.setMembresia(membresia);
                clienteExistente.setIdentificacion(identificacion);
                ClienteDAO.actualizarCliente(clienteExistente);
                mostrarInfo("Cliente modificado exitosamente.");
            }

            actualizarTabla();
            ventana.close();
        });

        VBox layout = new VBox(10,
                lblNombre, txtNombre,
                lblTelefono, txtTelefono,
                lblEmail, txtEmail,
                lblMembresia, cmbMembresia,
                lblIdentificacion, txtIdentificacion,
                btnGuardar
        );
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-padding: 20px;");

        Scene scene = new Scene(layout, 350, 450);
        ventana.setScene(scene);
        ventana.show();
    }

    private void eliminarCliente() {
        Cliente clienteSeleccionado = tableView.getSelectionModel().getSelectedItem();

        if (clienteSeleccionado == null) {
            mostrarError("Seleccione un cliente para eliminar.");
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar eliminación");
        confirmacion.setHeaderText(null);
        confirmacion.setContentText("¿Estás seguro de que deseas eliminar este cliente?");
        confirmacion.showAndWait().ifPresent(respuesta -> {
            if (respuesta == ButtonType.OK) {
                ClienteDAO.eliminarCliente(clienteSeleccionado.getId(), rolUsuario);
                actualizarTabla();
                mostrarInfo("Cliente eliminado correctamente.");
            }
        });
    }

    private void actualizarTabla() {
        clientesList.setAll(ClienteDAO.obtenerClientes());
        ordenarClientes();
    }

    private void ordenarClientes() {
        FXCollections.sort(clientesList, Comparator.comparing(Cliente::getNombre));
    }

    private boolean validarEmail(String email) {
        return Pattern.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$", email);
    }

    private boolean validarTelefono(String telefono) {
        return Pattern.matches("\\d+", telefono);
    }

    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void mostrarInfo(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Información");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}

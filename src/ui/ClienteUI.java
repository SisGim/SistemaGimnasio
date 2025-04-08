package ui;

import database.ClienteDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.Cliente;

public class ClienteUI {

    private TableView<Cliente> tableView = new TableView<>();
    private ObservableList<Cliente> clientesList = FXCollections.observableArrayList();
    private String rolUsuario; // 📌 Almacena el rol del usuario actual

    // Constructor que recibe el rol del usuario autenticado
    public ClienteUI(String rolUsuario) {
        this.rolUsuario = rolUsuario;
    }

    public void start(Stage primaryStage) {
        primaryStage.setTitle("Gestión de Clientes");

        configurarTabla(); // 📌 Agrega columnas a la tabla

        // Botones
        Button btnAgregar = new Button("Agregar Cliente");
        Button btnModificar = new Button("Modificar Cliente");
        Button btnEliminar = new Button("Eliminar Cliente");

        // 📌 Deshabilitar el botón eliminar si el usuario NO es administrador
        if (!"Administrador".equals(rolUsuario)) {
            btnEliminar.setDisable(true);
        }

        // Cargar clientes en la tabla
        clientesList.addAll(ClienteDAO.obtenerClientes());
        tableView.setItems(clientesList);

        // Asignar eventos a los botones
        btnAgregar.setOnAction(e -> agregarCliente());
        btnModificar.setOnAction(e -> modificarCliente());
        btnEliminar.setOnAction(e -> eliminarCliente());

        // Layout principal
        VBox layout = new VBox(10, tableView, btnAgregar, btnModificar, btnEliminar);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-padding: 20px;");

        // Configuración de la escena
        Scene scene = new Scene(layout, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // 📌 Configurar columnas de la tabla
    private void configurarTabla() {
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

        tableView.getColumns().addAll(colId, colNombre, colTelefono, colEmail, colMembresia);
    }

    // 📌 Método para agregar un cliente
    private void agregarCliente() {
        Stage ventana = new Stage();
        ventana.setTitle("Agregar Cliente");

        Label lblNombre = new Label("Nombre:");
        TextField txtNombre = new TextField();
        Label lblTelefono = new Label("Teléfono:");
        TextField txtTelefono = new TextField();
        Label lblEmail = new Label("Email:");
        TextField txtEmail = new TextField();
        Label lblMembresia = new Label("Membresía:");

        // 📌 Lista desplegable para seleccionar membresía
        ComboBox<String> cmbMembresia = new ComboBox<>();
        cmbMembresia.getItems().addAll("Básica", "Premium", "VIP", "Familiar");
        cmbMembresia.setValue("Básica");

        Button btnGuardar = new Button("Guardar");
        btnGuardar.setOnAction(e -> {
            String nombre = txtNombre.getText().trim();
            String telefono = txtTelefono.getText().trim();
            String email = txtEmail.getText().trim();
            String membresia = cmbMembresia.getValue();

            if (!nombre.isEmpty() && !telefono.isEmpty() && !email.isEmpty() && membresia != null) {
                Cliente nuevoCliente = new Cliente(0, nombre, telefono, email, membresia);
                ClienteDAO.agregarCliente(nuevoCliente);
                actualizarTabla();
                ventana.close();
            } else {
                mostrarError("Por favor, llena todos los campos.");
            }
        });

        VBox layout = new VBox(10, lblNombre, txtNombre, lblTelefono, txtTelefono, lblEmail, txtEmail, lblMembresia, cmbMembresia, btnGuardar);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-padding: 20px;");

        Scene scene = new Scene(layout, 300, 350);
        ventana.setScene(scene);
        ventana.show();
    }

    // 📌 Método para modificar un cliente
    private void modificarCliente() {
        Cliente clienteSeleccionado = tableView.getSelectionModel().getSelectedItem();

        if (clienteSeleccionado == null) {
            mostrarError("Seleccione un cliente para modificar.");
            return;
        }

        Stage ventana = new Stage();
        ventana.setTitle("Modificar Cliente");

        Label lblNombre = new Label("Nombre:");
        TextField txtNombre = new TextField(clienteSeleccionado.getNombre());

        Label lblTelefono = new Label("Teléfono:");
        TextField txtTelefono = new TextField(clienteSeleccionado.getTelefono());

        Label lblEmail = new Label("Email:");
        TextField txtEmail = new TextField(clienteSeleccionado.getEmail());

        Label lblMembresia = new Label("Membresía:");
        ComboBox<String> cmbMembresia = new ComboBox<>();
        cmbMembresia.getItems().addAll("Básica", "Premium", "VIP", "Familiar");
        cmbMembresia.setValue(clienteSeleccionado.getMembresia());

        Button btnGuardar = new Button("Guardar Cambios");
        btnGuardar.setOnAction(e -> {
            clienteSeleccionado.setNombre(txtNombre.getText().trim());
            clienteSeleccionado.setTelefono(txtTelefono.getText().trim());
            clienteSeleccionado.setEmail(txtEmail.getText().trim());
            clienteSeleccionado.setMembresia(cmbMembresia.getValue());

            ClienteDAO.actualizarCliente(clienteSeleccionado);
            actualizarTabla();
            ventana.close();
        });

        VBox layout = new VBox(10, lblNombre, txtNombre, lblTelefono, txtTelefono, lblEmail, txtEmail, lblMembresia, cmbMembresia, btnGuardar);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-padding: 20px;");

        Scene scene = new Scene(layout, 300, 350);
        ventana.setScene(scene);
        ventana.show();
    }

    // 📌 Método para eliminar un cliente (solo disponible para Administradores)
    private void eliminarCliente() {
        Cliente clienteSeleccionado = tableView.getSelectionModel().getSelectedItem();

        if (clienteSeleccionado == null) {
            mostrarError("Seleccione un cliente para eliminar.");
            return;
        }

        ClienteDAO.eliminarCliente(clienteSeleccionado.getId(), rolUsuario);
        actualizarTabla();
    }

    // 📌 Actualizar tabla después de cambios
    private void actualizarTabla() {
        clientesList.setAll(ClienteDAO.obtenerClientes());
    }

    // 📌 Método para mostrar errores
    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}

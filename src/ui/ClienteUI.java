package ui;

import database.AsignacionDAO;
import database.ClienteDAO;
import database.HistorialMembresiaDAO;
import database.MembresiaDAO;
import database.UsuarioDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.Cliente;
import models.HistorialMembresia;
import models.Membresia;
import util.CustomDialogUtil;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;

public class ClienteUI {

    private TableView<Cliente> tableView = new TableView<>();
    private ObservableList<Cliente> clientesList = FXCollections.observableArrayList();
    private String rolUsuario;
    private String correoEntrenador;

    public ClienteUI(String rolUsuario) {
        this(rolUsuario, null);
    }

    public ClienteUI(String rolUsuario, String correoEntrenador) {
        this.rolUsuario = rolUsuario;
        this.correoEntrenador = correoEntrenador;
    }

    public Node getVista() {
        configurarTabla();

        Button btnAgregar = new Button("Agregar Cliente");
        Button btnModificar = new Button("Modificar Cliente");
        Button btnEliminar = new Button("Eliminar Cliente");
        Button btnAsignar = new Button("Asignar a Entrenador");

        // Restricción: solo Admin puede eliminar o asignar
        if (!"Administrador".equalsIgnoreCase(rolUsuario)) {
            btnEliminar.setDisable(true);
            btnAsignar.setDisable(true);
        }

        // Carga de datos según rol
        if ("Entrenador".equalsIgnoreCase(rolUsuario)) {
            clientesList.addAll(ClienteDAO.obtenerClientesAsignados(correoEntrenador));
            btnAgregar.setDisable(true);
            btnEliminar.setDisable(true);
            btnAsignar.setDisable(true);
        } else {
            clientesList.addAll(ClienteDAO.obtenerClientes());
        }

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
        btnAsignar.setOnAction(e -> asignarClienteAEntrenador());

        VBox layout = new VBox(10, tableView, btnAgregar, btnModificar, btnEliminar, btnAsignar);
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

        TextField txtNombre = new TextField(esNuevo ? "" : clienteExistente.getNombre());
        TextField txtTelefono = new TextField(esNuevo ? "" : clienteExistente.getTelefono());
        TextField txtEmail = new TextField(esNuevo ? "" : clienteExistente.getEmail());
        TextField txtIdentificacion = new TextField(esNuevo ? "" : clienteExistente.getIdentificacion());

        ComboBox<Membresia> cmbMembresia = new ComboBox<>();
        List<Membresia> membresias = MembresiaDAO.obtenerMembresias();
        cmbMembresia.getItems().addAll(membresias);
        cmbMembresia.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Membresia item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getTipo());
            }
        });
        cmbMembresia.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Membresia item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getTipo());
            }
        });

        if (!esNuevo) {
            for (Membresia m : membresias) {
                if (m.getTipo().equals(clienteExistente.getMembresia())) {
                    cmbMembresia.setValue(m);
                    break;
                }
            }
        }

        Button btnGuardar = new Button(esNuevo ? "Guardar" : "Guardar Cambios");
        btnGuardar.setOnAction(e -> {
            String nombre = txtNombre.getText().trim();
            String telefono = txtTelefono.getText().trim();
            String email = txtEmail.getText().trim();
            String identificacion = txtIdentificacion.getText().trim();
            Membresia membresiaSeleccionada = cmbMembresia.getValue();

            if (nombre.isEmpty() || telefono.isEmpty() || email.isEmpty() || identificacion.isEmpty() || membresiaSeleccionada == null) {
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

            String tipoMembresia = membresiaSeleccionada.getTipo();

            if (esNuevo) {
                Cliente nuevoCliente = new Cliente(0, nombre, telefono, email, tipoMembresia, identificacion);
                ClienteDAO.agregarCliente(nuevoCliente);
                nuevoCliente = ClienteDAO.obtenerClientePorCorreo(email);
                registrarHistorial(nuevoCliente, membresiaSeleccionada);
                mostrarInfo("Cliente agregado exitosamente.");
            } else {
                clienteExistente.setNombre(nombre);
                clienteExistente.setTelefono(telefono);
                clienteExistente.setEmail(email);
                clienteExistente.setMembresia(tipoMembresia);
                clienteExistente.setIdentificacion(identificacion);
                ClienteDAO.actualizarCliente(clienteExistente);
                registrarHistorial(clienteExistente, membresiaSeleccionada);
                mostrarInfo("Cliente modificado exitosamente.");
            }

            actualizarTabla();
            ventana.close();
        });

        VBox layout = new VBox(10,
                new Label("Nombre:"), txtNombre,
                new Label("Teléfono:"), txtTelefono,
                new Label("Email:"), txtEmail,
                new Label("Membresía:"), cmbMembresia,
                new Label("Identificación:"), txtIdentificacion,
                btnGuardar
        );
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-padding: 20px;");

        ventana.setScene(new Scene(layout, 400, 500));
        ventana.show();
    }

    private void asignarClienteAEntrenador() {
        Cliente clienteSeleccionado = tableView.getSelectionModel().getSelectedItem();
        if (clienteSeleccionado == null) {
            mostrarError("Seleccione un cliente para asignar.");
            return;
        }

        List<String> entrenadores = UsuarioDAO.obtenerCorreosPorRol("Entrenador");
        if (entrenadores.isEmpty()) {
            mostrarError("No hay entrenadores disponibles.");
            return;
        }

        ChoiceDialog<String> dialogo = new ChoiceDialog<>(entrenadores.get(0), entrenadores);
        dialogo.setTitle("Asignar Cliente");
        dialogo.setHeaderText("Seleccione un entrenador:");
        dialogo.setContentText("Entrenador:");

        dialogo.showAndWait().ifPresent(entrenador -> {
            boolean exito = AsignacionDAO.asignarCliente(entrenador, clienteSeleccionado.getEmail());
            if (exito) {
                mostrarInfo("Cliente asignado correctamente.");
            } else {
                mostrarError("Error: este cliente ya está asignado.");
            }
        });
    }

    private void registrarHistorial(Cliente cliente, Membresia membresia) {
        HistorialMembresia h = new HistorialMembresia();
        h.setIdCliente(cliente.getId());
        h.setClienteEmail(cliente.getEmail());
        h.setTipoMembresia(membresia.getTipo());
        h.setFechaInicio(LocalDate.now().toString());
        h.setFechaFin(LocalDate.now().plusMonths(membresia.getDuracionMeses()).toString());
        h.setPrecio(membresia.getPrecio());
        HistorialMembresiaDAO.insertarHistorialMembresia(h);
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
        if ("Entrenador".equalsIgnoreCase(rolUsuario)) {
            clientesList.setAll(ClienteDAO.obtenerClientesAsignados(correoEntrenador));
        } else {
            clientesList.setAll(ClienteDAO.obtenerClientes());
        }
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

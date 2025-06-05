package ui;

import database.MembresiaDAO;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import models.Membresia;

public class MembresiaUI {

    private TableView<Membresia> tableView = new TableView<>();
    private ObservableList<Membresia> membresiasList = FXCollections.observableArrayList();
    private String rolUsuario;

    public MembresiaUI(String rolUsuario) {
        this.rolUsuario = rolUsuario;
    }

    public VBox getVista() {
        configurarTabla();

        Button btnAgregar = new Button("➕ Nueva Membresía");
        Button btnModificar = new Button("📝 Modificar Membresía");
        Button btnEliminar = new Button("❌ Eliminar Membresía");

        if (!"Administrador".equalsIgnoreCase(rolUsuario)) {
            btnEliminar.setDisable(true);
        }

        membresiasList.setAll(MembresiaDAO.obtenerMembresias());
        tableView.setItems(membresiasList);

        btnAgregar.setOnAction(e -> mostrarFormulario(null));
        btnModificar.setOnAction(e -> {
            Membresia seleccionada = tableView.getSelectionModel().getSelectedItem();
            if (seleccionada != null) {
                mostrarFormulario(seleccionada);
            } else {
                mostrarAlerta("Advertencia", "Selecciona una membresía para modificar.");
            }
        });

        btnEliminar.setOnAction(e -> eliminarMembresia());

        HBox botones = new HBox(15, btnAgregar, btnModificar, btnEliminar);
        botones.setAlignment(Pos.CENTER);

        VBox layout = new VBox(20, tableView, botones);
        layout.setStyle("-fx-padding: 25px; -fx-background-color: #111111;");
        layout.setAlignment(Pos.CENTER);

        return layout;
    }

    private void configurarTabla() {
        tableView.getColumns().clear();

        TableColumn<Membresia, Number> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(data -> data.getValue().idProperty());

        TableColumn<Membresia, String> colTipo = new TableColumn<>("Tipo");
        colTipo.setCellValueFactory(data -> data.getValue().tipoProperty());

        TableColumn<Membresia, Number> colDuracion = new TableColumn<>("Duración (días)");
        colDuracion.setCellValueFactory(data -> data.getValue().duracionProperty());

        TableColumn<Membresia, Number> colPrecio = new TableColumn<>("Precio ($)");
        colPrecio.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().calcularPrecio()));

        tableView.getColumns().addAll(colId, colTipo, colDuracion, colPrecio);
    }

    private void mostrarFormulario(Membresia existente) {
        Dialog<Membresia> dialog = new Dialog<>();
        dialog.setTitle(existente == null ? "Agregar Membresía" : "Modificar Membresía");

        Label lblTipo = new Label("Tipo:");
        TextField txtTipo = new TextField(existente != null ? existente.getTipo() : "");

        Label lblDuracion = new Label("Duración (días):");
        TextField txtDuracion = new TextField(existente != null ? String.valueOf(existente.getDuracion()) : "");

        Label lblPrecio = new Label("Precio Base ($):");
        TextField txtPrecio = new TextField(existente != null ? String.valueOf(existente.getPrecioBase()) : "");

        VBox contenido = new VBox(10, lblTipo, txtTipo, lblDuracion, txtDuracion, lblPrecio, txtPrecio);
        contenido.setAlignment(Pos.CENTER_LEFT);
        contenido.setStyle("-fx-background-color: #1e1e1e; -fx-padding: 20px;");
        lblTipo.setTextFill(Color.WHITE);
        lblDuracion.setTextFill(Color.WHITE);
        lblPrecio.setTextFill(Color.WHITE);

        dialog.getDialogPane().setContent(contenido);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(btn -> {
            if (btn == ButtonType.OK) {
                try {
                    String tipo = txtTipo.getText().trim();
                    int duracion = Integer.parseInt(txtDuracion.getText().trim());
                    double precioBase = Double.parseDouble(txtPrecio.getText().trim());

                    if (tipo.isEmpty() || duracion <= 0 || precioBase <= 0) {
                        throw new IllegalArgumentException("Todos los campos deben ser válidos.");
                    }

                    if (existente == null) {
                        Membresia nueva = new Membresia(0, tipo, precioBase, duracion);
                        MembresiaDAO.agregarMembresia(nueva);
                        mostrarAlerta("Éxito", "Membresía agregada correctamente.");
                    } else {
                        existente.setTipo(tipo);
                        existente.setDuracion(duracion);
                        existente.setPrecioBase(precioBase);
                        MembresiaDAO.actualizarMembresia(existente);
                        mostrarAlerta("Éxito", "Membresía actualizada correctamente.");
                    }

                    actualizarTabla();

                } catch (NumberFormatException ex) {
                    mostrarAlerta("Error", "Duración y precio deben ser números válidos.");
                } catch (IllegalArgumentException ex) {
                    mostrarAlerta("Error", ex.getMessage());
                }
            }
            return null;
        });

        dialog.showAndWait();
    }

    private void eliminarMembresia() {
        Membresia seleccionada = tableView.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            mostrarAlerta("Advertencia", "Selecciona una membresía para eliminar.");
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmación");
        confirmacion.setHeaderText("¿Estás seguro de eliminar esta membresía?");
        confirmacion.setContentText("Esta acción no se puede deshacer.");

        confirmacion.showAndWait().ifPresent(respuesta -> {
            if (respuesta == ButtonType.OK) {
                MembresiaDAO.eliminarMembresia(seleccionada.getId());
                actualizarTabla();
                mostrarAlerta("Éxito", "Membresía eliminada correctamente.");
            }
        });
    }

    private void actualizarTabla() {
        membresiasList.setAll(MembresiaDAO.obtenerMembresias());
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}

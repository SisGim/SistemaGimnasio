package ui;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.Membresia;
import database.MembresiaDAO;

public class MembresiaUI extends Application {

    private TableView<Membresia> tableView = new TableView<>();
    private ObservableList<Membresia> membresiasList = FXCollections.observableArrayList();
    private String rolUsuario = "Administrador"; // se toma por defecto que es administrador 'se puede cambiar para simular otro rol'

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Titan Forge - Gestión de Membresías");

        configurarTabla();

        Button btnAgregar = new Button("New Membership");
        Button btnModificar = new Button("Membership Modification");
        Button btnEliminar = new Button("Cancel Membership");

        if (!"Administrador".equals(rolUsuario)) {
            btnEliminar.setDisable(true); // solo el administrador puede eliminar
        }

        membresiasList.addAll(MembresiaDAO.obtenerMembresias());
        tableView.setItems(membresiasList);

        btnAgregar.setOnAction(e -> agregarMembresia());
        btnModificar.setOnAction(e -> modificarMembresia());
        btnEliminar.setOnAction(e -> eliminarMembresia());

        VBox layout = new VBox(10, tableView, btnAgregar, btnModificar, btnEliminar);
        layout.setAlignment(Pos.CENTER); // alinea el texto
        layout.setStyle("-fx-padding: 20px; -fx-background-color: #1E1E1E;"); // estilo del texto

        Scene scene = new Scene(layout, 600, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void configurarTabla() {
    TableColumn<Membresia, Number> colId = new TableColumn<>("Client ID");
    colId.setCellValueFactory(cellData -> cellData.getValue().idProperty());

    TableColumn<Membresia, String> colNombre = new TableColumn<>("Membership Type");
    colNombre.setCellValueFactory(cellData -> cellData.getValue().nombreProperty());

    TableColumn<Membresia, Number> colDuracion = new TableColumn<>("Duration (Days)");
    colDuracion.setCellValueFactory(cellData -> cellData.getValue().duracionProperty());

    TableColumn<Membresia, Number> colPrecio = new TableColumn<>("Price ($)"); // calculamos el precio
    colPrecio.setCellValueFactory(cellData -> 
        new SimpleDoubleProperty(cellData.getValue().calcularPrecio())
    );

    tableView.getColumns().addAll(colId, colNombre, colDuracion, colPrecio);
}

  

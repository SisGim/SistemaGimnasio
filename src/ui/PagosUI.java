package ui;

import database.ClienteDAO;
import database.HistorialMembresiaDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import models.Cliente;
import models.HistorialMembresia;

public class PagosUI {

    private final String rolUsuario;
    private final String correoUsuario;

    public PagosUI(String rolUsuario, String correoUsuario) {
        this.rolUsuario = rolUsuario;
        this.correoUsuario = correoUsuario;
    }

    public Node getVista() {
        VBox layout = new VBox(20);
        layout.setPadding(new Insets(30));
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: black;");

        Label titulo = new Label("💳 Gestión de Membresías");
        titulo.setStyle("-fx-text-fill: white; -fx-font-size: 22px;");
        layout.getChildren().add(titulo);

        if ("Cliente".equalsIgnoreCase(rolUsuario)) {
            Cliente cliente = ClienteDAO.obtenerClientePorCorreo(correoUsuario);
            if (cliente != null) {
                return new PagosClienteUI(cliente).getVista();
            } else {
                Label error = new Label("No se encontró la información del cliente.");
                error.setStyle("-fx-text-fill: red;");
                layout.getChildren().add(error);
                return layout;
            }
        }

        if ("Administrador".equalsIgnoreCase(rolUsuario)) {
            agregarVistaHistorialAdmin(layout);
            return layout;
        }

        Label sinPermiso = new Label("No tienes permisos para acceder a esta sección.");
        sinPermiso.setStyle("-fx-text-fill: red;");
        layout.getChildren().add(sinPermiso);
        return layout;
    }

    private void agregarVistaHistorialAdmin(VBox layout) {
        Label subtitulo = new Label("📋 Historial de Membresías Adquiridas");
        subtitulo.setStyle("-fx-text-fill: white; -fx-font-size: 18px;");

        TableView<HistorialMembresia> tabla = new TableView<>();
        ObservableList<HistorialMembresia> historial = FXCollections.observableArrayList(HistorialMembresiaDAO.obtenerHistorial());

        TableColumn<HistorialMembresia, String> colCliente = new TableColumn<>("Correo Cliente");
        colCliente.setCellValueFactory(cell -> cell.getValue().clienteEmailProperty());

        TableColumn<HistorialMembresia, String> colMembresia = new TableColumn<>("Membresía");
        colMembresia.setCellValueFactory(cell -> cell.getValue().tipoMembresiaProperty());

        TableColumn<HistorialMembresia, String> colInicio = new TableColumn<>("Inicio");
        colInicio.setCellValueFactory(cell -> cell.getValue().fechaInicioProperty());

        TableColumn<HistorialMembresia, String> colFin = new TableColumn<>("Fin");
        colFin.setCellValueFactory(cell -> cell.getValue().fechaFinProperty());

        TableColumn<HistorialMembresia, Number> colValor = new TableColumn<>("Valor Pagado ($)");
        colValor.setCellValueFactory(cell -> cell.getValue().valorTotalProperty());

        tabla.getColumns().addAll(colCliente, colMembresia, colInicio, colFin, colValor);
        tabla.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tabla.setItems(historial);

        if (historial.isEmpty()) {
            Label sinRegistros = new Label("No hay registros de membresías adquiridas.");
            sinRegistros.setStyle("-fx-text-fill: gray;");
            layout.getChildren().addAll(subtitulo, sinRegistros);
        } else {
            layout.getChildren().addAll(subtitulo, tabla);
        }
    }
}

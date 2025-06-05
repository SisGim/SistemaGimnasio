package ui;

import database.AsistenciaDAO;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import models.Asistencia;

public class ReportesUI {

    private final String rolUsuario;
    private final String correoUsuario;

    public ReportesUI(String rolUsuario, String correoUsuario) {
        this.rolUsuario = rolUsuario;
        this.correoUsuario = correoUsuario;
    }

    public Node getVista() {
        VBox layout = new VBox(20);
        layout.setPadding(new Insets(30));
        layout.setAlignment(Pos.TOP_CENTER);
        layout.setStyle("-fx-background-color: black;");

        Label titulo = new Label("📊 Módulo de Reportes");
        titulo.setStyle("-fx-text-fill: white; -fx-font-size: 22px;");
        layout.getChildren().add(titulo);

        if ("Administrador".equalsIgnoreCase(rolUsuario)) {
            Label lblAsistencias = new Label("📋 Control de Asistencias");
            lblAsistencias.setStyle("-fx-text-fill: white; -fx-font-size: 18px;");

            TableView<Asistencia> tablaAsistencias = new TableView<>();
            tablaAsistencias.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

            TableColumn<Asistencia, String> colCorreo = new TableColumn<>("Correo Cliente");
            TableColumn<Asistencia, String> colFecha = new TableColumn<>("Fecha y Hora");

            colCorreo.setCellValueFactory(data ->
                    new SimpleStringProperty(data.getValue().getCorreoCliente()));
            colFecha.setCellValueFactory(data ->
                    new SimpleStringProperty(data.getValue().getFechaHora().toString()));

            tablaAsistencias.getColumns().addAll(colCorreo, colFecha);
            tablaAsistencias.setItems(FXCollections.observableArrayList(
                    AsistenciaDAO.obtenerTodasLasAsistencias()
            ));

            layout.getChildren().addAll(lblAsistencias, tablaAsistencias);
        }

        return layout;
    }
}

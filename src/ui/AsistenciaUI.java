package ui;

import database.AsistenciaDAO;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.Asistencia;
import util.CustomDialogUtil;

import java.time.LocalDateTime;

public class AsistenciaUI extends Application {

    private String rolUsuario = "Cliente"; // Este valor debe pasarse dinámicamente al integrar en DashboardUI
    private String correoUsuario = "cliente@ejemplo.com"; // También debe ser pasado al abrir esta ventana

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("📋 Gestión de Asistencias");

        Label label = new Label("Pantalla de Gestión de Asistencias");
        label.setStyle("-fx-font-size: 18px;");

        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-padding: 30px; -fx-background-color: black;");
        layout.getChildren().add(label);

        if ("Cliente".equalsIgnoreCase(rolUsuario)) {
            Button btnRegistrar = new Button("Registrar Asistencia");
            btnRegistrar.setStyle("-fx-background-color: green; -fx-text-fill: white;");
            btnRegistrar.setOnAction(e -> {
                Asistencia asistencia = new Asistencia(correoUsuario, LocalDateTime.now());
                AsistenciaDAO.registrarAsistencia(asistencia);
                CustomDialogUtil.mostrarAlertaEstilizada(
                        "✅ Asistencia registrada",
                        "Tu asistencia ha sido registrada exitosamente.",
                        "Gracias por asistir hoy al gimnasio."
                );
            });
            layout.getChildren().add(btnRegistrar);
        }

        Button btnCerrar = new Button("Cerrar");
        btnCerrar.setOnAction(e -> primaryStage.close());
        layout.getChildren().add(btnCerrar);

        Scene scene = new Scene(layout, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Método auxiliar para lanzar la UI con parámetros
    public void mostrarVentana(String rolUsuario, String correoUsuario) {
        this.rolUsuario = rolUsuario;
        this.correoUsuario = correoUsuario;
        start(new Stage());
    }
}

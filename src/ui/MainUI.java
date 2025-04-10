package ui;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainUI extends Application {
    private String userRole;

    public void setUserRole(String role) {
        this.userRole = role; // Almacenar el rol del usuario
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Menú Principal");

        Button btnGestionClientes = new Button("Gestión de Clientes");
        Button btnGestionMembresias = new Button("Gestión de Membresías");
        Button btnControlPagos = new Button("Control de Pagos");
        Button btnRegistroAsistencia = new Button("Registro de Asistencia");
        Button btnGeneracionReportes = new Button("Generación de Reportes");
        Button btnGestionUsuarios = new Button("Gestión de Usuarios");

        // Ocultar botones según el rol
        if ("Empleado".equals(userRole)) {
            btnGeneracionReportes.setDisable(true);
            btnGestionUsuarios.setDisable(true);
        }
        if ("Empleado".equals(userRole)) {
            btnGestionClientes.setText("Gestión de Clientes (Sin eliminación)");
        }

        // Acción para cada botón
        btnGestionClientes.setOnAction(e -> new ClienteUI().start(new Stage()));
        btnGestionMembresias.setOnAction(e -> new MembresiaUI().start(new Stage()));
        btnControlPagos.setOnAction(e -> new PagosUI().start(new Stage()));
        btnRegistroAsistencia.setOnAction(e -> new AsistenciaUI().start(new Stage()));
        btnGeneracionReportes.setOnAction(e -> new ReportesUI().start(new Stage()));
        btnGestionUsuarios.setOnAction(e -> new UsuarioUI().start(new Stage()));

        VBox layout = new VBox(10, btnGestionClientes, btnGestionMembresias, btnControlPagos, btnRegistroAsistencia, btnGeneracionReportes, btnGestionUsuarios);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-padding: 20px;");

        Scene scene = new Scene(layout, 400, 350);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
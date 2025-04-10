package ui;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AsistenciaUI extends Application {
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Gestión de Asistencias");

        Label label = new Label("Pantalla de Gestión de Asistencias");
        Button btnCerrar = new Button("Cerrar");
        btnCerrar.setOnAction(e -> primaryStage.close());

        VBox layout = new VBox(20, label, btnCerrar);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-padding: 20px;");

        Scene scene = new Scene(layout, 400, 300);

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}

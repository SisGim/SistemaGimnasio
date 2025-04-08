package ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class MembresiaUI extends Application {
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Gestión de Membresías");

        Label label = new Label("Pantalla de Gestión de Membresías");
        StackPane layout = new StackPane(label);
        Scene scene = new Scene(layout, 400, 300);

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}

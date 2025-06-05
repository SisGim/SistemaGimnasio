package util;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class CustomDialogUtil {

    // Versión extendida con título, mensaje y detalle
    public static void mostrarAlertaEstilizada(String titulo, String mensaje, String detalle) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle(titulo);

        Label lblTitulo = new Label(titulo);
        lblTitulo.setFont(new Font("Arial Black", 20));
        lblTitulo.setTextFill(Color.web("#1DB954"));

        Label lblMensaje = new Label(mensaje);
        lblMensaje.setFont(new Font(16));
        lblMensaje.setTextFill(Color.WHITE);

        Label lblDetalle = new Label(detalle);
        lblDetalle.setFont(new Font(14));
        lblDetalle.setTextFill(Color.GRAY);

        Button btnCerrar = new Button("Aceptar");
        btnCerrar.setPrefWidth(120);
        btnCerrar.setStyle("-fx-background-color: #1DB954; -fx-text-fill: white; -fx-font-size: 14px;");
        btnCerrar.setOnAction(e -> dialog.close());

        VBox layout = new VBox(15, lblTitulo, lblMensaje, lblDetalle, btnCerrar);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(30));
        layout.setStyle("-fx-background-color: #111111; -fx-background-radius: 12;");

        Scene scene = new Scene(layout, 550, 250);
        dialog.setScene(scene);
        dialog.showAndWait();
    }

    // Versión simple solo con mensaje
    public static void mostrarAlertaEstilizada(String mensaje) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Atención");

        Label lblTitulo = new Label("🔔 Aviso");
        lblTitulo.setFont(new Font("Arial Black", 20));
        lblTitulo.setTextFill(Color.web("#1DB954"));

        Label lblMensaje = new Label(mensaje);
        lblMensaje.setFont(new Font(16));
        lblMensaje.setTextFill(Color.WHITE);
        lblMensaje.setWrapText(true);

        Button btnCerrar = new Button("Aceptar");
        btnCerrar.setPrefWidth(120);
        btnCerrar.setStyle("-fx-background-color: #1DB954; -fx-text-fill: white; -fx-font-size: 14px;");
        btnCerrar.setOnAction(e -> dialog.close());

        VBox layout = new VBox(20, lblTitulo, lblMensaje, btnCerrar);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(30));
        layout.setStyle("-fx-background-color: #111111; -fx-background-radius: 12;");

        Scene scene = new Scene(layout, 500, 220);
        dialog.setScene(scene);
        dialog.showAndWait();
    }
}

package ui;

import database.ClienteDAO;
import database.HistorialMembresiaDAO;
import database.MembresiaDAO;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import models.Cliente;
import models.HistorialMembresia;
import models.Membresia;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PagosClienteUI {

    private final Cliente cliente;
    private final ComboBox<Membresia> cmbMembresias = new ComboBox<>();
    private final Label lblVencimiento = new Label();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public PagosClienteUI(Cliente cliente) {
        this.cliente = cliente;
    }

    public Node getVista() {
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(25));
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: black;");

        Label lblTitulo = new Label("💳 Gestión de Membresía");
        lblTitulo.setFont(new Font("Arial Black", 18));
        lblTitulo.setStyle("-fx-text-fill: white;");

        Label lblActual = new Label("Membresía actual: " + cliente.getMembresia());
        lblActual.setStyle("-fx-text-fill: white;");

        Label lblSeleccion = new Label("Selecciona una nueva membresía:");
        lblSeleccion.setStyle("-fx-text-fill: white;");

        List<Membresia> disponibles = MembresiaDAO.obtenerMembresias();
        cmbMembresias.getItems().addAll(disponibles);
        cmbMembresias.setPromptText("Selecciona...");
        cmbMembresias.setStyle("-fx-background-color: white; -fx-border-radius: 5;");

        lblVencimiento.setStyle("-fx-text-fill: #1DB954;");

        cmbMembresias.setOnAction(e -> calcularFechaVencimiento());

        Button btnConfirmar = new Button("✅ Confirmar Compra/Renovación");
        btnConfirmar.setStyle("-fx-background-color: #1DB954; -fx-text-fill: white;");
        btnConfirmar.setOnAction(e -> procesarCompra());

        layout.getChildren().addAll(lblTitulo, lblActual, lblSeleccion, cmbMembresias, lblVencimiento, btnConfirmar);

        return layout;
    }

    private void calcularFechaVencimiento() {
        Membresia seleccionada = cmbMembresias.getValue();
        if (seleccionada != null) {
            LocalDate hoy = LocalDate.now();
            LocalDate vencimiento = hoy.plusDays(seleccionada.getDuracion());
            lblVencimiento.setText("Tu nueva membresía vencerá el: " + vencimiento.format(formatter));
        }
    }

    private void procesarCompra() {
        Membresia seleccionada = cmbMembresias.getValue();
        if (seleccionada == null) {
            mostrarAlerta("Debes seleccionar una membresía.");
            return;
        }

        LocalDate hoy = LocalDate.now();
        LocalDate vencimiento = hoy.plusDays(seleccionada.getDuracion());

        HistorialMembresia registro = new HistorialMembresia();
        registro.setIdCliente(cliente.getId());
        registro.setClienteEmail(cliente.getEmail());
        registro.setTipoMembresia(seleccionada.getTipo());
        registro.setFechaInicio(hoy.toString());
        registro.setFechaFin(vencimiento.toString());
        registro.setPrecio(seleccionada.calcularPrecio());

        HistorialMembresiaDAO.insertarHistorialMembresia(registro);

        cliente.setMembresia(seleccionada.getTipo());
        ClienteDAO.actualizarCliente(cliente);

        mostrarInfo("¡Compra o renovación exitosa!\nVálida hasta: " + vencimiento.format(formatter));
    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Alerta");
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

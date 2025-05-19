package ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class DashboardUI {

    private BorderPane root;
    private String rol;

    public DashboardUI(String rol) {
        this.rol = rol;
    }

    public void start(Stage stage) {
        stage.setTitle("Titan Forge - Panel Principal");

        root = new BorderPane();
        root.setStyle("-fx-background-color: black;");

        VBox sideMenu = crearSideMenu();
        root.setLeft(sideMenu);

        Label lblBienvenida = new Label("Bienvenido al sistema Titan Forge");
        lblBienvenida.setFont(new Font("Arial", 18));
        lblBienvenida.setStyle("-fx-text-fill: white;");
        StackPane center = new StackPane(lblBienvenida);
        center.setAlignment(Pos.CENTER);
        root.setCenter(center);

        Scene scene = new Scene(root, 1280, 720);
        stage.setScene(scene);
        stage.show();
    }

    private VBox crearSideMenu() {
        Label lblTitulo = new Label("⚛️ TITAN\nFORGE");
        lblTitulo.setFont(new Font("Arial Black", 20));
        lblTitulo.setStyle("-fx-text-fill: white;");

        Button btnUsuarios = crearBoton("👤 Usuarios", () -> mostrarUsuarios());
        Button btnClientes = crearBoton("🧾 Clientes", () -> mostrarClientes());
        Button btnMaquinas = crearBoton("🛠️ Máquinas", () -> mostrarSeccion("Módulo de Máquinas"));
        Button btnPagos = crearBoton("💳 Pagos", () -> mostrarSeccion("Módulo de Pagos"));
        Button btnReportes = crearBoton("📊 Reportes", () -> mostrarSeccion("Módulo de Reportes"));
        Button btnCerrarSesion = crearBoton("⛔ Cerrar sesión", () -> System.exit(0));

        VBox menu = new VBox(15, lblTitulo, btnUsuarios, btnClientes, btnMaquinas, btnPagos, btnReportes, btnCerrarSesion);
        menu.setAlignment(Pos.TOP_CENTER);
        menu.setPadding(new Insets(25));
        menu.setStyle("-fx-background-color: #111111;");
        menu.setPrefWidth(220);

        return menu;
    }

    private Button crearBoton(String texto, Runnable accion) {
        Button btn = new Button(texto);
        btn.setPrefWidth(180);
        btn.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-border-color: #1DB954;" +
            "-fx-border-width: 2;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 15px;" +
            "-fx-background-radius: 8;" +
            "-fx-border-radius: 8;"
        );
        btn.setOnAction(e -> accion.run());
        return btn;
    }

    private void mostrarSeccion(String mensaje) {
        Label label = new Label(mensaje);
        label.setStyle("-fx-text-fill: white; -fx-font-size: 18px;");
        StackPane contenedor = new StackPane(label);
        contenedor.setAlignment(Pos.CENTER);
        root.setCenter(contenedor);
    }

    private void mostrarUsuarios() {
        GestionUsuariosUI gestion = new GestionUsuariosUI();
        root.setCenter(gestion.getVista());
    }

    private void mostrarClientes() {
        ClienteUI clientes = new ClienteUI(rol);
        root.setCenter(clientes.getVista());
    }
}

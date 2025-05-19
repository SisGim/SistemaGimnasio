package ui;

import database.ClienteDAO;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import models.Cliente;
import util.CustomDialogUtil;

public class DashboardUI {

    private BorderPane root;
    private String rol;
    private String correoUsuario;

    public DashboardUI(String rol, String correoUsuario) {
        this.rol = rol;
        this.correoUsuario = correoUsuario;
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

        if ("Cliente".equalsIgnoreCase(rol)) {
            Cliente cliente = ClienteDAO.obtenerClientePorCorreo(correoUsuario);
            if (cliente != null &&
                (esPorCompletar(cliente.getNombre()) ||
                 esPorCompletar(cliente.getTelefono()) ||
                 esPorCompletar(cliente.getIdentificacion()))) {

                CustomDialogUtil.mostrarAlertaEstilizada(
                        "Información incompleta",
                        "Debes completar tu perfil antes de usar el sistema.",
                        "Haz clic en el ícono de perfil (arriba a la izquierda) para completar tus datos."
                );
                mostrarPerfil();
            }
        }

        Scene scene = new Scene(root, 1280, 720);
        stage.setScene(scene);
        stage.show();
    }

    private VBox crearSideMenu() {
        ImageView perfilIcon = new ImageView(new Image(getClass().getResourceAsStream("/resources/user_icon.png")));
        perfilIcon.setFitHeight(35);
        perfilIcon.setFitWidth(35);
        perfilIcon.setOnMouseClicked(e -> mostrarPerfil());

        Label lblTitulo = new Label("⚛️ TITAN\nFORGE");
        lblTitulo.setFont(new Font("Arial Black", 20));
        lblTitulo.setStyle("-fx-text-fill: white;");

        VBox header = new VBox(perfilIcon, lblTitulo);
        header.setAlignment(Pos.CENTER);
        header.setSpacing(10);

        Button btnUsuarios = crearBoton("👤 Usuarios", this::mostrarUsuarios);
        Button btnClientes = crearBoton("🧾 Clientes", this::mostrarClientes);
        Button btnMaquinas = crearBoton("🛠️ Máquinas", () -> mostrarSeccion("Módulo de Máquinas"));
        Button btnPagos = crearBoton("💳 Pagos", () -> mostrarSeccion("Módulo de Pagos"));
        Button btnReportes = crearBoton("📊 Reportes", () -> mostrarSeccion("Módulo de Reportes"));
        Button btnCerrarSesion = crearBoton("⛔ Cerrar sesión", () -> System.exit(0));

        VBox menu = new VBox(15, header, btnUsuarios, btnClientes, btnMaquinas, btnPagos, btnReportes, btnCerrarSesion);
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
        btn.setOnAction(e -> {
            if (tieneDatosIncompletos()) {
                CustomDialogUtil.mostrarAlertaEstilizada(
                        "Información incompleta",
                        "Debes completar tu perfil antes de usar el sistema.",
                        "Haz clic en el ícono de perfil (arriba a la izquierda) para completar tus datos."
                );
                mostrarPerfil();
                return;
            }
            accion.run();
        });
        return btn;
    }

    private boolean tieneDatosIncompletos() {
        if (!"Cliente".equalsIgnoreCase(rol)) return false;
        Cliente cliente = ClienteDAO.obtenerClientePorCorreo(correoUsuario);
        if (cliente == null) return false;
        return esPorCompletar(cliente.getNombre()) ||
               esPorCompletar(cliente.getTelefono()) ||
               esPorCompletar(cliente.getIdentificacion());
    }

    private boolean esPorCompletar(String campo) {
        return campo == null || campo.trim().isEmpty() || campo.equalsIgnoreCase("Por completar");
    }

    private void mostrarSeccion(String mensaje) {
        Label label = new Label(mensaje);
        label.setStyle("-fx-text-fill: white; -fx-font-size: 18px;");
        StackPane contenedor = new StackPane(label);
        contenedor.setAlignment(Pos.CENTER);
        root.setCenter(contenedor);
    }

    private void mostrarUsuarios() {
        root.setCenter(new GestionUsuariosUI().getVista());
    }

    private void mostrarClientes() {
        root.setCenter(new ClienteUI(rol).getVista());
    }

    private void mostrarPerfil() {
        root.setCenter(new PerfilUsuarioUI(correoUsuario));
    }
}

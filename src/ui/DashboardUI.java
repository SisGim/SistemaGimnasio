package ui;

import database.ClienteDAO;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
            if (cliente != null
                    && (esPorCompletar(cliente.getNombre())
                    || esPorCompletar(cliente.getTelefono())
                    || esPorCompletar(cliente.getIdentificacion()))) {

                CustomDialogUtil.mostrarAlertaEstilizada("Debes completar tu perfil antes de usar el sistema.\nHaz clic en el ícono de perfil para actualizar tus datos.");
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

        VBox menu = new VBox(15);
        menu.getChildren().add(header);

        if ("Administrador".equalsIgnoreCase(rol) || "Empleado".equalsIgnoreCase(rol)) {
            menu.getChildren().addAll(
                    crearBoton("👤 Usuarios", this::mostrarUsuarios),
                    crearBoton("🧾 Clientes", this::mostrarClientes),
                    crearBoton("🏷️ Membresías", this::mostrarMembresias),
                    crearBoton("🛠️ Máquinas", () -> mostrarSeccion("Módulo de Máquinas")),
                    crearBoton("💳 Pagos", this::mostrarPagos), // ✅ corregido
                    crearBoton("📊 Reportes", () -> mostrarSeccion("Módulo de Reportes"))
            );
        }

        if ("Cliente".equalsIgnoreCase(rol)) {
            menu.getChildren().addAll(
                    crearBoton("🛠️ Máquinas", () -> mostrarSeccion("Módulo de Máquinas")),
                    crearBoton("💳 Membresía", this::mostrarPagoMembresia)
            );
        }

        menu.getChildren().add(crearBoton("⛔ Cerrar sesión", () -> System.exit(0)));

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
                "-fx-background-color: transparent;"
                        + "-fx-border-color: #1DB954;"
                        + "-fx-border-width: 2;"
                        + "-fx-text-fill: white;"
                        + "-fx-font-size: 15px;"
                        + "-fx-background-radius: 8;"
                        + "-fx-border-radius: 8;"
        );
        btn.setOnAction(e -> {
            if (tieneDatosIncompletos()) {
                CustomDialogUtil.mostrarAlertaEstilizada("Debes completar tu perfil antes de usar el sistema.\nHaz clic en el ícono de perfil para actualizar tus datos.");
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
        return cliente != null && (
                esPorCompletar(cliente.getNombre()) ||
                esPorCompletar(cliente.getTelefono()) ||
                esPorCompletar(cliente.getIdentificacion())
        );
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

    private void mostrarMembresias() {
        root.setCenter(new MembresiaUI(rol).getVista());
    }

    private void mostrarPerfil() {
        root.setCenter(new PerfilUsuarioUI(correoUsuario));
    }

    private void mostrarPagoMembresia() {
        root.setCenter(new PagosUI(rol, correoUsuario).getVista());
    }

    // ✅ Nuevo método para Admin/Empleado
    private void mostrarPagos() {
        root.setCenter(new PagosUI(rol, correoUsuario).getVista());
    }
}

package ui;

import database.ClienteDAO;
import database.HistorialMembresiaDAO;
import database.UsuarioDAO;
import database.AsistenciaDAO;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
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
import models.HistorialMembresia;
import models.Usuario;
import models.Asistencia;
import util.CustomDialogUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

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

        if (debeCompletarPerfil()) {
            CustomDialogUtil.mostrarAlertaEstilizada("Debes completar tu perfil antes de usar el sistema.\nHaz clic en el ícono de perfil para actualizar tus datos.");
            mostrarPerfil();
        }

        if ("Cliente".equalsIgnoreCase(rol)) {
            // Registrar asistencia al iniciar sesión
            Asistencia asistencia = new Asistencia(correoUsuario, LocalDateTime.now());
            AsistenciaDAO.registrarAsistencia(asistencia);

            Cliente cliente = ClienteDAO.obtenerClientePorCorreo(correoUsuario);
            if (cliente != null) {
                List<HistorialMembresia> historial = HistorialMembresiaDAO.obtenerHistorialPorIdCliente(cliente.getId());
                if (!historial.isEmpty()) {
                    HistorialMembresia ultima = historial.get(0);
                    LocalDate fechaFin = LocalDate.parse(ultima.getFechaFin());
                    long diasRestantes = ChronoUnit.DAYS.between(LocalDate.now(), fechaFin);
                    if (diasRestantes >= 0 && diasRestantes <= 5) {
                        CustomDialogUtil.mostrarAlertaEstilizada("⚠️ Tu membresía está próxima a vencer el "
                                + fechaFin.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + ".\n¡Renueva a tiempo para no perder el acceso!");
                    }
                }
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
                    crearBoton("🛠️ Máquinas", this::mostrarModuloMaquinas),
                    crearBoton("💳 Pagos", this::mostrarPagos),
                    crearBoton("📊 Reportes", this::mostrarReportes)
            );
        }

        if ("Cliente".equalsIgnoreCase(rol)) {
            menu.getChildren().addAll(
                    crearBoton("🛠️ Máquinas", this::mostrarModuloMaquinas),
                    crearBoton("💳 Membresía", this::mostrarPagoMembresia)
            );
        }

        if ("Entrenador".equalsIgnoreCase(rol)) {
            menu.getChildren().add(
                    crearBoton("🧾 Mis Clientes", this::mostrarClientesAsignados)
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
            if (debeCompletarPerfil()) {
                CustomDialogUtil.mostrarAlertaEstilizada("Debes completar tu perfil antes de usar el sistema.\nHaz clic en el ícono de perfil para actualizar tus datos.");
                mostrarPerfil();
                return;
            }
            accion.run();
        });
        return btn;
    }

    private boolean debeCompletarPerfil() {
        if ("Cliente".equalsIgnoreCase(rol)) {
            Cliente cliente = ClienteDAO.obtenerClientePorCorreo(correoUsuario);
            return cliente == null || contienePorCompletar(cliente.getNombre(), cliente.getTelefono(), cliente.getIdentificacion());
        }
        if ("Entrenador".equalsIgnoreCase(rol)) {
            Usuario usuario = UsuarioDAO.obtenerUsuarioPorCorreo(correoUsuario);
            return usuario == null || contienePorCompletar(usuario.getNombre(), usuario.getTelefono(), usuario.getIdentificacion());
        }
        return false;
    }

    private boolean contienePorCompletar(String... campos) {
        for (String campo : campos) {
            if (campo == null || campo.trim().isEmpty() || campo.toLowerCase().contains("por completar")) {
                return true;
            }
        }
        return false;
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

    private void mostrarClientesAsignados() {
        root.setCenter(new ClienteUI("Entrenador", correoUsuario).getVista());
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

    private void mostrarPagos() {
        root.setCenter(new PagosUI(rol, correoUsuario).getVista());
    }

    private void mostrarModuloMaquinas() {
        if (!"Cliente".equalsIgnoreCase(rol)) {
            mostrarSeccion("Módulo de Máquinas");
            return;
        }

        Cliente cliente = ClienteDAO.obtenerClientePorCorreo(correoUsuario);
        if (cliente == null) {
            CustomDialogUtil.mostrarAlertaEstilizada("Error al verificar estado de membresía.");
            return;
        }

        List<HistorialMembresia> historial = HistorialMembresiaDAO.obtenerHistorialPorIdCliente(cliente.getId());
        if (historial.isEmpty()) {
            CustomDialogUtil.mostrarAlertaEstilizada("No tienes una membresía activa. Renueva tu plan para acceder.");
            return;
        }

        LocalDate vencimiento = LocalDate.parse(historial.get(0).getFechaFin());
        if (vencimiento.isBefore(LocalDate.now())) {
            CustomDialogUtil.mostrarAlertaEstilizada("Tu membresía ha vencido el "
                    + vencimiento.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + ". Renueva para continuar.");
            return;
        }

        TableView<String[]> tabla = new TableView<>();

        TableColumn<String[], String> colMaquina = new TableColumn<>("Máquina");
        colMaquina.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue()[0]));

        TableColumn<String[], String> colEjercicio = new TableColumn<>("Ejercicio");
        colEjercicio.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue()[1]));

        tabla.getColumns().addAll(colMaquina, colEjercicio);
        tabla.getItems().addAll(
                FXCollections.observableArrayList(
                        new String[]{"Bicicleta Estática", "Cardio de calentamiento (10 min)"},
                        new String[]{"Prensa de Piernas", "4x12 Repeticiones"},
                        new String[]{"Remo", "4x10 Repeticiones"},
                        new String[]{"Pecho en máquina", "3x15 Repeticiones"},
                        new String[]{"Máquina de abdominales", "3x20 Repeticiones"}
                )
        );

        VBox layout = new VBox(20, new Label("🏋️ Ejercicios Recomendados"), tabla);
        layout.setPadding(new Insets(30));
        layout.setAlignment(Pos.CENTER);
        root.setCenter(layout);
    }

    private void mostrarReportes() {
        root.setCenter(new ReportesUI(rol, correoUsuario).getVista());
    }
}

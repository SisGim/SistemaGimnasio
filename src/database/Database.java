package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
    // 📌 Ruta del archivo de la base de datos en modo embebido
    private static final String URL = "jdbc:h2:C:/Users/Juan Diego Rojas/NetBeansProjects/SistemaGimnasio/database/gimnasioDB";

    private static final String USER = "GYM"; // Usuario por defecto de H2
    private static final String PASSWORD = "2025"; // Contraseña

    public static Connection connect() throws SQLException {
        try {
            Class.forName("org.h2.Driver"); // Registrar el driver H2
        } catch (ClassNotFoundException e) {
            throw new SQLException("Error: Driver de H2 no encontrado.", e);
        }

        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void initializeDatabase() {
        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            String sqlUsuarios = "CREATE TABLE IF NOT EXISTS usuarios (" +
                                 "id INT AUTO_INCREMENT PRIMARY KEY, " +
                                 "username VARCHAR(50) UNIQUE NOT NULL, " +
                                 "password VARCHAR(100) NOT NULL, " +
                                 "rol VARCHAR(20) NOT NULL)";
            stmt.execute(sqlUsuarios);

            String sqlClientes = "CREATE TABLE IF NOT EXISTS clientes (" +
                                 "id INT AUTO_INCREMENT PRIMARY KEY, " +
                                 "nombre VARCHAR(100) NOT NULL, " +
                                 "telefono VARCHAR(20) NOT NULL, " +
                                 "email VARCHAR(100) NOT NULL, " +
                                 "membresia VARCHAR(50) NOT NULL)";
            stmt.execute(sqlClientes);

            System.out.println("Base de datos conectada y funcionando en modo embebido.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

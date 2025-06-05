package tests;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class TestDatabase {
    private static final String URL = "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1";
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    @BeforeAll
    public static void initialize() {
        try {
            initializeDatabase();
        } catch (SQLException e) {
            Assertions.fail("Failed to initialize database: " + e.getMessage());
        }
    }

    public static void initializeDatabase() throws SQLException {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            // Create users table
            String usersSql = "CREATE TABLE IF NOT EXISTS users (" +
                "id INT PRIMARY KEY AUTO_INCREMENT," +
                "username VARCHAR(50) NOT NULL," +
                "password VARCHAR(255) NOT NULL," +
                "role VARCHAR(20) NOT NULL)";
            
            // Create clients table
            String clientsSql = "CREATE TABLE IF NOT EXISTS clientes (" +
                "id INT PRIMARY KEY AUTO_INCREMENT," +
                "nombre VARCHAR(100) NOT NULL," +
                "telefono VARCHAR(20)," +
                "email VARCHAR(100)," +
                "membresia VARCHAR(50)," +
                "identificacion VARCHAR(20) UNIQUE)";
            
            // Create membership catalog table
            String membresiasSql = "CREATE TABLE IF NOT EXISTS membresias (" +
                "id INT PRIMARY KEY AUTO_INCREMENT," +
                "tipo VARCHAR(50) NOT NULL," +
                "precio_base DECIMAL(10,2) NOT NULL," +
                "duracion INT NOT NULL," +
                "descripcion TEXT)";
            
            try (Statement stmt = conn.createStatement()) {
                stmt.execute(usersSql);
                stmt.execute(clientsSql);
                stmt.execute(membresiasSql);
                
                // Insert default memberships
                stmt.execute("INSERT INTO membresias (id, tipo, precio_base, duracion, descripcion) VALUES (1, 'Básica', 50.00, 1, 'Membresía básica')");
                stmt.execute("INSERT INTO membresias (id, tipo, precio_base, duracion, descripcion) VALUES (2, 'Premium', 100.00, 3, 'Membresía premium')");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error initializing test database", e);
        }
    }

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    @Test
    public void testDatabaseConnection() {
        Connection conn = null;
        try {
            conn = connect();
            Assertions.assertNotNull(conn, "Connection should not be null");
        } catch (SQLException e) {
            Assertions.fail("Database connection failed: " + e.getMessage());
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    Assertions.fail("Failed to close connection: " + e.getMessage());
                }
            }
        }
    }

    public static void main(String[] args) {
        try {
            initializeDatabase();
            System.out.println("Test database initialized successfully");
        } catch (SQLException e) {
            System.err.println("Error initializing test database: " + e.getMessage());
        }
    }
}
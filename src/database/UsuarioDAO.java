package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    public static boolean registrarUsuario(String username, String password) {
        try (Connection conn = Database.connect()) {
            // Verificar si el usuario ya existe
            PreparedStatement checkStmt = conn.prepareStatement(
                    "SELECT COUNT(*) FROM usuarios WHERE USERNAME = ?"
            );
            checkStmt.setString(1, username);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                return false; // Usuario ya existe
            }

            // Insertar nuevo usuario
            System.out.println("[DEBUG] Contraseña registrada: " + password); // TEMPORAL
            PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO usuarios (USERNAME, PASSWORD, ROL) VALUES (?, ?, 'cliente')"
            );
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.executeUpdate();

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean actualizarRol(String username, String nuevoRol) {
        try (Connection conn = Database.connect()) {
            PreparedStatement stmt = conn.prepareStatement(
                    "UPDATE usuarios SET ROL = ? WHERE USERNAME = ?"
            );
            stmt.setString(1, nuevoRol);
            stmt.setString(2, username);
            int filas = stmt.executeUpdate();
            return filas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<String> obtenerUsuarios() {
        List<String> usuarios = new ArrayList<>();
        try (Connection conn = Database.connect()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT USERNAME FROM usuarios WHERE ROL != 'Administrador'");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                usuarios.add(rs.getString("USERNAME"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return usuarios;
    }

    // Obtener rol actual de un usuario
    public static String obtenerRol(String username) {
        try (Connection conn = Database.connect()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT ROL FROM usuarios WHERE USERNAME = ?");
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("ROL");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String verificarCredenciales(String username, String password) {
        String sql = "SELECT ROL FROM usuarios WHERE USERNAME = ? AND PASSWORD = ?";

        try (Connection conn = Database.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username.trim());
            pstmt.setString(2, password.trim());

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getString("ROL");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null; // Credenciales incorrectas
    }
}
